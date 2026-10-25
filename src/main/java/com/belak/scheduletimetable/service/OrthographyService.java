package com.belak.scheduletimetable.service;

import org.languagetool.JLanguageTool;
import org.languagetool.language.French;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

@Service
public class OrthographyService {

    private final JLanguageTool languageTool;

    public OrthographyService() {
        this.languageTool = new JLanguageTool(French.getInstance());
    }

    public String correctText(String text) throws IOException {

        if (text == null || text.isBlank()) {
            return text;
        }

        List<RuleMatch> matches = languageTool.check(text);

        StringBuilder corrected = new StringBuilder(text);

        // Parcourir les erreurs de la fin vers le début
        for (int i = matches.size() - 1; i >= 0; i--) {

            RuleMatch match = matches.get(i);

            if (match.getSuggestedReplacements().isEmpty()) {
                continue;
            }

            String original = corrected.substring(
                    match.getFromPos(),
                    match.getToPos()
            );

            String suggestion = match.getSuggestedReplacements().get(0);

            // Ne pas modifier les accents présents dans le texte original
            suggestion = preserveAccents(original, suggestion);

            corrected.replace(
                    match.getFromPos(),
                    match.getToPos(),
                    suggestion
            );
        }

        return corrected.toString();
    }

    private String preserveAccents(String original, String suggestion) {

        String originalWithoutAccents = removeAccents(original);
        String suggestionWithoutAccents = removeAccents(suggestion);

        // Si les différences concernent uniquement les accents,
        // conserver le texte original.
        if (originalWithoutAccents.equalsIgnoreCase(suggestionWithoutAccents)) {
            return original;
        }

        return suggestion;
    }

    private String removeAccents(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}