public final class VigenereCipher {
    private static final String KEY = "TMU";

    private VigenereCipher() {}

    public static String encrypt(String plaintext) {
        return transform(plaintext, true);
    }

    public static String decrypt(String ciphertext) {
        return transform(ciphertext, false);
    }

    private static String transform(String input, boolean encrypt) {
        if (input == null) return null;

        StringBuilder out = new StringBuilder(input.length());
        int keyIdx = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isLetter(c)) {
                boolean upper = Character.isUpperCase(c);
                char base = upper ? 'A' : 'a';

                int pi = Character.toLowerCase(c) - 'a';               // 0..25
                int ki = Character.toUpperCase(KEY.charAt(keyIdx % KEY.length())) - 'A'; // 0..25

                int ci = encrypt ? (pi + ki) % 26 : (pi - ki + 26) % 26;
                char enc = (char) (base + ci);

                out.append(enc);
                keyIdx++; // advance key only on letters
            } else {
                out.append(c); // keep spaces, punctuation, digits unchanged
            }
        }

        return out.toString();
    }
}
