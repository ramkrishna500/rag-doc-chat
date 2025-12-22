package com.ramkrushna.ragdocchat.util;

public class TextSplitter {

    private static final int CHUNK_SIZE = 200;
    private static final int OVERLAP = 20;

    public static void splitAndConsume(String text,
            java.util.function.Consumer<String> consumer) {

        if (text == null || text.isBlank())
            return;

        String[] words = text.split("\\s+");

        int start = 0;

        while (start < words.length) {

            int end = Math.min(start + CHUNK_SIZE, words.length);

            StringBuilder chunk = new StringBuilder(1024);
            for (int i = start; i < end; i++) {
                chunk.append(words[i]).append(" ");
            }

            consumer.accept(chunk.toString().trim());

            // 🔴 CRITICAL FIX
            if (end == words.length) {
                break; // EXIT LOOP
            }

            start = end - OVERLAP;
            if (start < 0) {
                start = 0;
            }
        }
    }
}
