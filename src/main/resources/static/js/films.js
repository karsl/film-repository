const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

// Film table's search functionality.
function search() {
    let filter = document.getElementById("filmSearch").value.toUpperCase();
    let trs = document.getElementById("filmTable").getElementsByTagName("tr");

    for (let i = 1; i < trs.length - 1; i += 2) {
        let fields = trs[i].getElementsByTagName("td");

        let filmName = fields[0].innerHTML;
        let genre = fields[2].innerHTML;
        let actorNames = Array.from(trs[i + 1].getElementsByClassName("actor-name"))
        .concat(Array.from(trs[i + 1].getElementsByClassName("actor-role")))
        .map(s => s.innerHTML);

        let texts = actorNames.concat([filmName, genre]);

        trs[i].style.display = trs[i + 1].style.display = texts.some(s => s.toUpperCase().includes(filter)) ? "" : "none";
    }
}

// Film table's sorting functionality.
const SortOrder = Object.freeze({
    UNSORTED: Symbol("unsorted"),
    ASCENDING: Symbol("ascending"),
    DESCENDING: Symbol("descending")
});

// Initially, release year is unsorted.
let yearSortOrder = SortOrder.UNSORTED;

// Slightly modified from: https://www.w3schools.com/howto/howto_js_sort_table.asp
function sortTableByYear() {
    var table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("filmTable");
    switching = true;
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < rows.length - 3; i += 2) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[3];
            y = rows[i + 2].getElementsByTagName("td")[3];

            if (yearSortOrder == SortOrder.UNSORTED || yearSortOrder == SortOrder.DESCENDING) {
                if (Number(x.innerHTML.toLowerCase()) > Number(y.innerHTML.toLowerCase())) {
                    shouldSwitch = true;
                    break;
                }
            } else if (yearSortOrder == SortOrder.ASCENDING) {
                if (Number(x.innerHTML.toLowerCase()) < Number(y.innerHTML.toLowerCase())) {
                    shouldSwitch = true;
                    break;
                }
            }
        }

        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 2], rows[i]);
            rows[i].parentNode.insertBefore(rows[i + 3], rows[i + 1]);
            switching = true;
        }
    }

    yearSortOrder = yearSortOrder == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING;
}

// Year picker for form.
$("#releaseYear").datepicker({
    format: "yyyy",
    viewMode: "years",
    minViewMode: "years"
});

// Deleting a film/row from film table.
$("#filmTable").on('click', '.deleteButton', (args) => {
    let filmId = $(args.currentTarget).closest("tr").attr("class");

    $.ajax({
        url: '/films/' + filmId,
        headers: { [header]: token },
        method: 'DELETE',
        success: () => {
            $('#filmTable .' + filmId).remove();
        }
    });
});

// Add a credit row in the form.
$("#creditAdd").click(() => {
    let newRow = document.getElementById('creditTable').getElementsByTagName('tbody')[0].insertRow();

    newRow.innerHTML = `
        <td>
            <input type="text" class="actorNameInput form-control" placeholder="Name" name="actorName[]" required/>
        </td>
        <td>
            <input type="text" class="actorRoleInput form-control" placeholder="Role" name="actorRole[]" required/>
        </td>
        <td>
            <button class="btn btn-danger deleteCredit" type="button">
                <i class="fas fa-trash"></i>
            </button>
        </td>
    `;
});


$("#creditTable").on('click', '.deleteCredit', (args) => {
    $(args.currentTarget).closest("tr").remove();
});

// TODO Remove this later.
function addCredit(actorFullName, actorRole) {
    let li = document.createElement("li");

    let actorNameSpan = document.createElement("span");
    actorNameSpan.classList.add("actor-name");
    actorNameSpan.innerHTML = actorFullName;

    let actorRoleSpan = document.createElement("span");
    actorRoleSpan.classList.add("actor-role");
    actorRoleSpan.innerHTML = actorRole;

    li.append(actorNameSpan, document.createTextNode(" as "), actorRoleSpan);

    return li;
}

// Submit the form and insert a new row to the film table.
$('#submitFormButton').on('click', (e) => {
    e.preventDefault();

    let form = document.querySelector('#newFilmForm');

    if (!form.checkValidity()) {
        e.stopPropagation();

        form.classList.add('was-validated');
    } else {
        // form.classList.remove('was-validated');
        let serializeOptions = {'encodes': {'number': true}};

        let data = $('#newFilmForm').serializeObject(serializeOptions);

        if (!('languages' in data)) {
            data.languages = []
        }

        let id = document.querySelector('#filmModal').getAttribute(
            'data-filmid');
        if (id != null) {
            data['id'] = Number(id);
        }
        data['year'] = Number(data['year']);
        data['languages'] = data['languages'].map(Number);
        data['genre'] = Number(data['genre']);
        data['media'] = Number(data['media']);

        data['credits'] = [];

        for (let i = 0; i < data['actorName'].length; i++) {
            data['credits'].push({
                'creditName': data['actorName'][i],
                'creditRole': data['actorRole'][i]
            });
        }

        // Update
        if (data['id']) {
            $.ajax({
                url: '/submitForm',
                headers: {[header]: token},
                contentType: 'application/json',
                data: JSON.stringify(data),
                method: 'PUT',
                success: (response) => {
                    console.log(response);

                    // Remove the corresponding rows from the table.
                    [...document.querySelectorAll(
                        `#filmTable tbody tr[class='${response.id}'`)].forEach(
                        r => r.remove());

                    addFilmToFilmTable(response);

                    $('#filmModal').modal('hide');
                },
                error: (response) => {
                    console.log(response);
                }
            });
        }

        // Insert
        else {
            debugger;
            $.ajax({
                url: '/submitForm',
                headers: {[header]: token},
                contentType: 'application/json',
                data: JSON.stringify(data),
                method: 'POST',
                success: (response) => {
                    addFilmToFilmTable(response);

                    $('#filmModal').modal('hide');
                },
                error: (error) => {
                    console.log(error);
                }
            })
        }
    }
});

function addFilmToFilmTable(film) {
    let tbody = document.querySelector('#filmTable tbody');
    let newFilmRow = tbody.insertRow();
    let newCreditRow = tbody.insertRow();

    newFilmRow.classList.add(film.id);
    newFilmRow.id = 'film' + film.id;
    newCreditRow.classList.add(film.id);
    newCreditRow.id = 'credit' + film.id;

    newFilmRow.innerHTML = `
                <td>${film.title}</td>
                <td>${film.description}</td>
                <td>${film.genre.name}</td>
                <td>${film.year}</td>
                <td>${film.media.name}</td>
                <td>
                    <button class="btn btn-info" type="button" data-toggle="collapse"
                            data-target="${'.collapse' + film.id}">
                        <i class="fas fa-info"></i>
                    </button>
                    <button class="btn btn-warning updateButton" type="button">
                        <i class="fas fa-pen"></i>
                    </button>
                    <button class="btn btn-danger deleteButton" type="button">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
           `;

    newCreditRow.innerHTML = `
                <tr class="${film.id}" id="${'credit' + film.id}">
                    <td colspan="6" class="${'collapse collapse' + film.id}">
                        <div class="${'collapse collapse' + film.id}">
                            <div class="card card-body">
                                <p>
                                    Available in languages:
                                    <span>${film.languages.map(l => l.name).join(', ') + '.'}</span>
                                </p>
                                <ul id="${'creditList' + film.id}"></ul>
                            </div>
                        </div>
                    </td>
                </tr>
            `;

    let creditList = document.getElementById('creditList' + film.id);

    film.credits.forEach(c => creditList.appendChild(addCredit(c.actor.fullName, c.role)));
}

function getEmptyFilm() {
    return ({'genre': {}, 'media': {}, 'languages': [], 'credits': []});
}

function mapFilmIdFilmObject(id) {
    let filmRow = document.querySelector(`#filmTable tbody tr#film${id}`);
    let detailsRow = document.querySelector(`#filmTable tbody tr#credit${id}`);

    let film = getEmptyFilm();

    let filmRowTdValues = Array.from(filmRow.querySelectorAll("td")).slice(0, -1).map(t => t.innerHTML);

    film.title = filmRowTdValues[0];
    film.description = filmRowTdValues[1];
    film.genre.name = filmRowTdValues[2];
    film.year = Number(filmRowTdValues[3]);
    film.media.name = filmRowTdValues[4];

    film.languages = detailsRow.querySelector('p span').innerText.trim()
    .slice(0, -1) // Drop the dot at the end
    .split(',')
    .map(n => ({'name': n.trim()}));

    film.credits = Array.from(detailsRow.querySelectorAll(`#creditList${id} li`)).map(l => ({
        'role': l.querySelector('.actor-role').innerHTML,
        'actor': {'name': l.querySelector('.actor-name').innerHTML}
    }));

    return film;
}

function bindToForm(film) {
    let modal = document.querySelector('#filmModal');

    modal.querySelector('#filmTitle').value = film.title ?? '';
    modal.querySelector('#releaseYear').value = film.year ?? 2021;
    modal.querySelector('#filmDescription').value = film.description ?? '';

    let languageNames = film.languages.map(l => l.name);
    let languageOptions = [...modal.querySelectorAll('#filmLanguages option')];
    // Clear prior selections
    languageOptions.forEach(o => o.selected = null);
    languageOptions.filter(o => languageNames.includes(o.innerHTML))
    .forEach(o => o.selected = 'selected');

    // To clear prior selection, select the first option.
    let genreOptions = [...modal.querySelectorAll('#filmGenre option')];
    genreOptions[0].selected = 'selected';
    if (film.genre) {
        genreOptions
        .filter(o => o.innerHTML == film.genre.name)
        .forEach(o => o.selected = 'selected');
    }

    modal.querySelector('#filmMedia input').checked = true;
    if (film.media) {
        [...modal.querySelectorAll('#filmMedia label')].filter(l => l.innerHTML == film.media.name).forEach(l => {
            let id = l.getAttribute('for');
            modal.querySelector(`#${id}`).checked = true;
        });
    }

    let tbody = modal.querySelector('#filmCredits table tbody');

    const [firstActor, ...restActors] = film.credits;

    let firstActorName, firstActorRole;

    if (firstActor) {
        // First actor part
        firstActorName = firstActor.actor.name;
        firstActorRole = firstActor.role;
    }

    // Clear prior actors
    tbody.innerHTML = `
      <tr>
        <td>
          <input type="text" class="actorNameInput form-control" placeholder="Name" name="actorName[]" value="${firstActorName ?? ''}" required/>
        </td>
        <td>
          <input type="text" class="actorRoleInput form-control" placeholder="Role" name="actorRole[]" value="${firstActorRole ?? ''}" required/>
        </td>
        <td>
        </td>
      </tr>
    `;

    // Rest of actors part.
    restActors.forEach(c => {
        let actorName = c.actor.name;
        let actorRole = c.role;

        let newRow = tbody.insertRow();

        newRow.innerHTML = `
            <td>
                <input type="text" class="actorNameInput form-control" placeholder="Name" name="actorName[]" value="${actorName}"/>
            </td>
            <td>
                <input type="text" class="actorRoleInput form-control" placeholder="Role" name="actorRole[]" value="${actorRole}"/>
            </td>
            <td>
                <button class="btn btn-danger deleteCredit" type="button">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
    });
}

$("#filmTable").on('click', '.updateButton', (args) => {
    let filmId = Number($(args.currentTarget).closest("tr").attr('class'));

    let film = mapFilmIdFilmObject(filmId);

    bindToForm(film);

    document.querySelector('#filmModalLabel').innerHTML = 'Update film';

    let filmModal = document.querySelector('#filmModal');
    filmModal.setAttribute('data-filmid', filmId);

    $('#filmModal').modal('toggle');
});

$('#showModalButton').click(() => {
    bindToForm(getEmptyFilm());

    document.querySelector('#filmModalLabel').innerHTML = 'Add new film';

    let filmModal = document.querySelector('#filmModal');
    filmModal.removeAttribute('data-filmId');

    $('#filmModal').modal('toggle');
});

$('#filmModal').on('hidden.bs.modal', (e) => {
    document.querySelector('#newFilmForm').classList.remove('was-validated');
});