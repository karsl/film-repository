package com.github.karsl.filmrepository;

import com.github.karsl.filmrepository.controller.FormSubmission;
import com.github.karsl.filmrepository.model.Credit;
import com.github.karsl.filmrepository.model.Language;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormSubmissionTest {

    @Test
    void testId() {
        Long testId = 1L;
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setId(testId);

        assertEquals(formSubmission.mapToFilm().getId(), testId);
    }

    @Test
    void testTitle() {
        String testTitle = "Test title";
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setTitle(testTitle);

        assertEquals(formSubmission.mapToFilm().getTitle(), testTitle);
    }

    @Test
    void testDescription() {
        String testDescription = "Test description";
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setDescription(testDescription);

        assertEquals(formSubmission.mapToFilm().getDescription(), testDescription);
    }

    @Test
    void testYear() {
        Integer testYear = 2021;
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setYear(testYear);

        assertEquals(formSubmission.mapToFilm().getYear(), testYear);
    }

    @Test
    void testLanguages() {
        List<Long> languageIds = List.of(1L, 2L);
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setLanguages(languageIds);

        Set<Long> mappedIds = formSubmission.mapToFilm().getLanguages().stream()
                .map(Language::getId).collect(Collectors.toSet());

        assertTrue(mappedIds.containsAll(languageIds));
        assertTrue(languageIds.containsAll(mappedIds));
    }

    @Test
    void testGenre() {
        Long genreId = 2L;
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setGenre(genreId);

        assertEquals(formSubmission.mapToFilm().getGenre().getId(), genreId);
    }

    @Test
    void testMedia() {
        Long mediaId = 2L;
        FormSubmission formSubmission = new FormSubmission();

        formSubmission.setMedia(mediaId);

        assertEquals(formSubmission.mapToFilm().getMedia().getId(), mediaId);
    }

    @Test
    void testCreditsRoles() {
        List<FormSubmission.CreditTuple> creditTuples = new ArrayList<>(Arrays.asList(
                new FormSubmission.CreditTuple("creditName1", "creditRole1"),
                new FormSubmission.CreditTuple("creditName2", "creditRole2")));

        FormSubmission formSubmission = new FormSubmission();
        formSubmission.setCredits(creditTuples);

        creditTuples.sort(Comparator.comparing(FormSubmission.CreditTuple::getCreditName));
        List<Credit> mappedCreditsSortedByActorName = formSubmission.mapToFilm().getCredits().stream()
                .sorted(Comparator.comparing(credit -> credit.getActor().getFullName())).collect(Collectors.toList());

        assertEquals(creditTuples.size(), mappedCreditsSortedByActorName.size());

        for (int i = 0; i < creditTuples.size(); i++) {
            Credit currentMappedCredit = mappedCreditsSortedByActorName.get(i);
            FormSubmission.CreditTuple currentCredit = creditTuples.get(i);

            assertEquals(currentMappedCredit.getActor().getFullName(), currentCredit.getCreditName());
            assertEquals(currentMappedCredit.getRole(), currentCredit.getCreditRole());
        }
    }

}
