const token = $('meta[name="_csrf"]').attr('content');
const header = $('meta[name="_csrf_header"]').attr('content');

// Film table's search functionality.
function search() {
  const filter = document.getElementById('filmSearch').value.toUpperCase();
  const trs = document.getElementById('filmTable').getElementsByTagName('tr');

  for (let i = 1; i < trs.length - 1; i += 2) {
    const fields = trs[i].getElementsByTagName('td');

    const filmName = fields[0].innerHTML;
    const genre = fields[2].innerHTML;
    const actorNames =
        [...trs[i + 1].getElementsByClassName('actor-name'),
          ...trs[i + 1].getElementsByClassName('actor-role')]
            .map((s) => s.innerHTML);

    const texts = actorNames.concat([filmName, genre]);

    trs[i].style.display = trs[i + 1].style.display = texts.some(
        (s) => s.toUpperCase().includes(filter)) ? '' : 'none';
  }
}

// Film table's sorting functionality.
const SortOrder = Object.freeze({
  UNSORTED: Symbol('unsorted'),
  ASCENDING: Symbol('ascending'),
  DESCENDING: Symbol('descending'),
});

// Initially, release year is unsorted.
let yearSortOrder = SortOrder.UNSORTED;

// Slightly modified from: https://www.w3schools.com/howto/howto_js_sort_table.asp
function sortTableByYear() {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById('filmTable');
  switching = true;
  while (switching) {
    switching = false;
    rows = table.rows;
    for (i = 1; i < rows.length - 3; i += 2) {
      shouldSwitch = false;
      x = rows[i].getElementsByTagName('td')[3];
      y = rows[i + 2].getElementsByTagName('td')[3];

      if (yearSortOrder === SortOrder.UNSORTED ||
          yearSortOrder === SortOrder.DESCENDING) {
        if (Number(x.innerHTML.toLowerCase()) > Number(
            y.innerHTML.toLowerCase())) {
          shouldSwitch = true;
          break;
        }
      } else if (yearSortOrder == SortOrder.ASCENDING) {
        if (Number(x.innerHTML.toLowerCase()) < Number(
            y.innerHTML.toLowerCase())) {
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

  yearSortOrder = yearSortOrder === SortOrder.ASCENDING ? SortOrder.DESCENDING :
      SortOrder.ASCENDING;
}

// Year picker for form.
$('#releaseYear').datepicker({
  format: 'yyyy',
  viewMode: 'years',
  minViewMode: 'years',
});

$('#filmModal').on('hidden.bs.modal', (e) => {
  document.querySelector('#newFilmForm').classList.remove('was-validated');
  $('#errorPanel').hide();
});

// Don't remove the alert from DOM.
$('#errorPanel').on('close.bs.alert', (e) => {
  $(e.currentTarget).hide();
  return false;
});

function alertError(message) {
  // The message is an array of messages, i.e. possibly multiple messages, then
  // just pick the first one.
  const messageToDisplay = JSON.parse(message)[0];

  document.querySelector('#errorText').innerHTML = messageToDisplay;
  document.querySelector('#errorPanel').style = '';
}

// Deleting a film/row from film table.
$('#filmTable').on('click', '.deleteButton', (args) => {
  const filmId = $(args.currentTarget).closest('tr').attr('class');

  $.ajax({
    url: '/films/' + filmId,
    headers: {[header]: token},
    method: 'DELETE',
    success: () => {
      $('#filmTable .' + filmId).remove();
    },
  });
}).on('click', '.updateButton', (args) => {
  const filmId = Number($(args.currentTarget).closest('tr').attr('class'));

  const film = mapFilmIdFilmObject(filmId);

  bindToForm(film);

  document.querySelector('#filmModalLabel').innerHTML = 'Update film';

  const filmModal = document.querySelector('#filmModal');
  filmModal.setAttribute('data-filmid', filmId);

  $('#filmModal').modal('toggle');
});

// Add a credit row in the form.
$('#creditAdd').click(() => {
  const newRow = document.getElementById('creditTable')
      .getElementsByTagName('tbody')[0].insertRow();

  newRow.innerHTML = `
        <td>
            <input type="text" class="actorNameInput form-control"
              placeholder="Name" name="actorName[]" required/>
        </td>
        <td>
            <input type="text" class="actorRoleInput form-control"
              placeholder="Role" name="actorRole[]" required/>
        </td>
        <td>
            <button class="btn btn-danger deleteCredit" type="button">
                <i class="fas fa-trash"></i>
            </button>
        </td>
    `;
});

$('#creditTable').on('click', '.deleteCredit', (args) => {
  $(args.currentTarget).closest('tr').remove();
});

// Submit the form and insert a new row to the film table.
$('#submitFormButton').on('click', (e) => {
  e.preventDefault();

  const form = document.querySelector('#newFilmForm');

  if (!form.checkValidity()) {
    e.stopPropagation();

    form.classList.add('was-validated');
  } else {
    // form.classList.remove('was-validated');
    const serializeOptions = {'encodes': {'number': true}};

    const data = $('#newFilmForm').serializeObject(serializeOptions);

    if (!('languages' in data)) {
      data.languages = [];
    }

    const id = document.querySelector('#filmModal').getAttribute(
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
        'creditRole': data['actorRole'][i],
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
          // Remove the corresponding rows from the table.
          [...document.querySelectorAll(
              `#filmTable tbody tr[class='${data.id}'`)]
              .forEach((r) => r.remove());

          document.querySelector('#filmTable tbody').innerHTML += response;

          $('#filmModal').modal('hide');
        },
        error: (response) => {
          alertError(response.responseText);
        },
      });
      // Insert
    } else {
      $.ajax({
        url: '/submitForm',
        headers: {[header]: token},
        contentType: 'application/json',
        data: JSON.stringify(data),
        method: 'POST',
        success: (response) => {
          document.querySelector('#filmTable tbody').innerHTML += response;
          $('#filmModal').modal('hide');
        },
        error: (response) => {
          alertError(response.responseText);
        },
      });
    }
  }
});

function getEmptyFilm() {
  return ({'genre': {}, 'media': {}, 'languages': [], 'credits': []});
}

function mapFilmIdFilmObject(id) {
  const filmRow = document.querySelector(`#filmTable tbody tr#film${id}`);
  const detailsRow = document.querySelector(`#filmTable tbody tr#credit${id}`);

  const film = getEmptyFilm();

  const filmRowTdValues = Array.from(filmRow.querySelectorAll('td')).slice(0,
      -1).map((t) => t.innerHTML);

  film.title = filmRowTdValues[0];
  film.description = filmRowTdValues[1];
  film.genre.name = filmRowTdValues[2];
  film.year = Number(filmRowTdValues[3]);
  film.media.name = filmRowTdValues[4];

  film.languages = detailsRow.querySelector('p span').innerText.trim()
      .slice(0, -1) // Drop the dot at the end
      .split(',')
      .map((n) => ({'name': n.trim()}));

  film.credits = Array.from(
      detailsRow.querySelectorAll(`#creditList${id} li`)).map((l) => ({
    'role': l.querySelector('.actor-role').innerHTML,
    'actor': {'name': l.querySelector('.actor-name').innerHTML},
  }));

  return film;
}

function bindToForm(film) {
  const modal = document.querySelector('#filmModal');

  modal.querySelector('#filmTitle').value = film.title ?? '';
  modal.querySelector('#releaseYear').value = film.year ?? 2021;
  modal.querySelector('#filmDescription').value = film.description ?? '';

  const languageNames = film.languages.map((l) => l.name);
  const languageOptions = [...modal.querySelectorAll('#filmLanguages option')];
  // Clear prior selections
  languageOptions.forEach((o) => o.selected = null);
  languageOptions.filter((o) => languageNames.includes(o.innerHTML))
      .forEach((o) => o.selected = 'selected');

  // To clear prior selection, select the first option.
  const genreOptions = [...modal.querySelectorAll('#filmGenre option')];
  genreOptions[0].selected = 'selected';
  if (film.genre) {
    genreOptions
        .filter((o) => o.innerHTML == film.genre.name)
        .forEach((o) => o.selected = 'selected');
  }

  modal.querySelector('#filmMedia input').checked = true;
  if (film.media) {
    [...modal.querySelectorAll('#filmMedia label')].filter(
        (l) => l.innerHTML == film.media.name).forEach((l) => {
      const id = l.getAttribute('for');
      modal.querySelector(`#${id}`).checked = true;
    });
  }

  const tbody = modal.querySelector('#filmCredits table tbody');

  const [firstActor, ...restActors] = film.credits;

  let firstActorName;
  let firstActorRole;

  if (firstActor) {
    // First actor part
    firstActorName = firstActor.actor.name;
    firstActorRole = firstActor.role;
  }

  // Clear prior actors
  tbody.innerHTML = `
      <tr>
        <td>
          <input type="text" class="actorNameInput form-control" 
            placeholder="Name" name="actorName[]"
            value="${firstActorName ?? ''}" required/>
        </td>
        <td>
          <input type="text" class="actorRoleInput form-control" 
            placeholder="Role" name="actorRole[]"
            value="${firstActorRole ?? ''}" required/>
        </td>
        <td>
        </td>
      </tr>
    `;

  // Rest of actors part.
  restActors.forEach((c) => {
    const actorName = c.actor.name;
    const actorRole = c.role;

    const newRow = tbody.insertRow();

    newRow.innerHTML = `
            <td>
                <input type="text" class="actorNameInput form-control" 
                  placeholder="Name" name="actorName[]" value="${actorName}"/>
            </td>
            <td>
                <input type="text" class="actorRoleInput form-control"
                  placeholder="Role" name="actorRole[]" value="${actorRole}"/>
            </td>
            <td>
                <button class="btn btn-danger deleteCredit" type="button">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
  });
}

$('#showModalButton').click(() => {
  bindToForm(getEmptyFilm());

  document.querySelector('#filmModalLabel').innerHTML = 'Add new film';

  const filmModal = document.querySelector('#filmModal');
  filmModal.removeAttribute('data-filmId');

  $('#filmModal').modal('toggle');
});

