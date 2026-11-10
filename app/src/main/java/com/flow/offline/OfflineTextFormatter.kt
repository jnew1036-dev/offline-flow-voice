package com.flow.offline

object OfflineTextFormatter {
    fun formatRawSpeech(rawText: String): String {
        var text = rawText.trim()
        if (text.isEmpty()) return ""

        // Filler words remove karna
        val fillers = listOf("umm", "uhh", "matlab", "yaani", "like actually")
        for (filler in fillers) {
            text = text.replace(Regex("(?i)\\b$filler\\b"), "")
        }

        // Voice punctuation rules
        text = text.replace(Regex("(?i)\\b(nayi line|new line)\\b"), "\n")
                   .replace(Regex("(?i)\\b(point|bullet point)\\b"), "\n• ")
                   .replace(Regex("(?i)\\b(full stop|khatam)\\b"), ". ")
                   .replace(Regex("(?i)\\b(comma|alpaviram)\\b"), ", ")
                   .replace(Regex("(?i)\\b(question mark|sawal)\\b"), "? ")

        text = text.replace(Regex("\\s+"), " ")
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
