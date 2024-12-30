
import java.util.Arrays;
import java.util.Scanner;

public class AES{
    static GF28 gf28 = new GF28();
        final static int[][] SBOX = {
        {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
        {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
        {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
        {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
        {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
        {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
        {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
        {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
        {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
        {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
        {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
        {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
        {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
        {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
        {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
        {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
        };

    
        final static int[][] INVERSE_SBOX = {
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
        };
        final static int[] RCON = new int[] {
            0x01000000, 0x02000000, 0x04000000, 0x08000000,
            0x10000000, 0x20000000, 0x40000000, 0x80000000,
            0x1b000000, 0x36000000
        };

        // Constants

    private static final int Nb = 4; // Block size (4 words)
    private static final int Nk = 4; // Key size (4 words for AES-128)
    private static final int Nr = 10; // Number of rounds for AES-128

    static int[][] keyExpansion(byte[] key) {
        int[] w = new int[Nb * (Nr + 1)];
        int i = 0;
    
        // Initialize the first Nk words
        while (i < Nk) {
            w[i] = ((key[4 * i] & 0xFF) << 24) |
                   ((key[4 * i + 1] & 0xFF) << 16) |
                   ((key[4 * i + 2] & 0xFF) << 8) |
                   (key[4 * i + 3] & 0xFF);
            i++;
        }
    
        // Generate the rest of the words
        i = Nk;
        while (i < Nb * (Nr + 1)) {
            int temp = w[i - 1];
            if (i % Nk == 0) {
                int rconIndex = (i / Nk) - 1; // Calculate the RCON index
                if (rconIndex < RCON.length) {
                    temp = subWord(rotWord(temp)) ^ (RCON[rconIndex] << 24);
                } else {
                    throw new IllegalStateException("RCON index out of bounds: " + rconIndex);
                }
            }
            w[i] = w[i - Nk] ^ temp;
            i++;
        }
    
        // Convert 1D array into 2D array for the expanded key
        int[][] expandedKey = new int[Nr + 1][Nb];
        for (i = 0; i < Nr + 1; i++) {
            System.arraycopy(w, i * Nb, expandedKey[i], 0, Nb);
        }
        return expandedKey;
    }
    

    // RotWord rotates a word left by 1 byte
    private static int rotWord(int word) {
        return (word << 8) | (word >>> 24);
    }

    // Function to substitute a single word (4 bytes) using S-Box
    public static int subWord(int word) {
        int result = 0;
        for (int i = 3; i >= 0; i--) {
            int byteValue = (word >> (i * 8)) & 0xFF;
            int row = (byteValue >> 4) & 0x0F;
            int col = byteValue & 0x0F;
            result = (result << 8) | SBOX[row][col];
        }
        return result;
    }

    // Cipher (Encryption)
    static byte[] encrypt(byte[] plaintext, byte[] key) {
        int[][] state = toState(plaintext);
        int[][] roundKeys = keyExpansion(key);
        //convert the key to 2d array
        // int[][] roundKeys = new int[Nr + 1][Nb];
        // for (int i = 0; i < Nr + 1; i++) {
        //     System.arraycopy(key, i * Nb, roundKeys[i], 0, Nb);
        // }

        addRoundKey(state, roundKeys[0]);
        
        for (int round = 1; round < Nr; round++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, roundKeys[round]);
        }

        subBytes(state);
        shiftRows(state);
        addRoundKey(state, roundKeys[Nr]);

        return fromState(state);
    }

    // Inverse Cipher (Decryption)
    static byte[] decrypt(byte[] ciphertext, byte[] key) {
        int[][] state = toState(ciphertext);
        int[][] roundKeys = keyExpansion(key);

        addRoundKey(state, roundKeys[Nr]);

        for (int round = Nr - 1; round > 0; round--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, roundKeys[round]);
            invMixColumns(state);
        }

        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, roundKeys[0]);

        return fromState(state);
    }

    // Function to substitute bytes in a 4x4 state array using S-Box
    public static void subBytes(int[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int byteValue = state[i][j];
                int row = (byteValue >> 4) & 0x0F;
                int col = byteValue & 0x0F;
                state[i][j] = SBOX[row][col];
            }
        }
    }


    // Function to substitute bytes in a 4x4 state array using Inverse S-Box
    public static void invSubBytes(int[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int byteValue = state[i][j];
                int row = (byteValue >> 4) & 0x0F;
                int col = byteValue & 0x0F;
                state[i][j] = INVERSE_SBOX[row][col];
            }
        }
    }

    static void shiftRows(int[][] state) {
        for (int i = 1; i < 4; i++) {
            int[] temp = new int[4];
            for (int j = 0; j < 4; j++) {
                temp[j] = state[i][(j + i) % 4];
            }
            System.arraycopy(temp, 0, state[i], 0, 4);
        }
    }

    // Inverse ShiftRows Transformation
    static void invShiftRows(int[][] state) {
        for (int i = 1; i < 4; i++) {
            int[] temp = new int[4];
            for (int j = 0; j < 4; j++) {
                temp[j] = state[i][(j - i + 4) % 4];
            }
            System.arraycopy(temp, 0, state[i], 0, 4);
        }
    }

    // MixColumns Transformation
    static void mixColumns(int[][] state) {
        for (int i = 0; i < 4; i++) {
            int[] temp = new int[4];
            temp[0] = gf28.multiply(state[0][i], 0x02) ^ gf28.multiply(state[1][i], 0x03) ^ state[2][i] ^ state[3][i];
            temp[1] = state[0][i] ^ gf28.multiply(state[1][i], 0x02) ^ gf28.multiply(state[2][i], 0x03) ^ state[3][i];
            temp[2] = state[0][i] ^ state[1][i] ^ gf28.multiply(state[2][i], 0x02) ^ gf28.multiply(state[3][i], 0x03);
            temp[3] = gf28.multiply(state[0][i], 0x03) ^ state[1][i] ^ state[2][i] ^ gf28.multiply(state[3][i], 0x02);
            for (int j = 0; j < 4; j++) {
                state[j][i] = temp[j];
            }
        }
    }

    // Inverse MixColumns Transformation
    static void invMixColumns(int[][] state) {
        for (int i = 0; i < 4; i++) {
            int[] temp = new int[4];
            temp[0] = gf28.multiply(state[0][i], 0x0E) ^ gf28.multiply(state[1][i], 0x0B) ^ gf28.multiply(state[2][i], 0x0D) ^ gf28.multiply(state[3][i], 0x09);
            temp[1] = gf28.multiply(state[0][i], 0x09) ^ gf28.multiply(state[1][i], 0x0E) ^ gf28.multiply(state[2][i], 0x0B) ^ gf28.multiply(state[3][i], 0x0D);
            temp[2] = gf28.multiply(state[0][i], 0x0D) ^ gf28.multiply(state[1][i], 0x09) ^ gf28.multiply(state[2][i], 0x0E) ^ gf28.multiply(state[3][i], 0x0B);
            temp[3] = gf28.multiply(state[0][i], 0x0B) ^ gf28.multiply(state[1][i], 0x0D) ^ gf28.multiply(state[2][i], 0x09) ^ gf28.multiply(state[3][i], 0x0E);
            for (int j = 0; j < 4; j++) 
            {
                state[j][i] = temp[j];
            }
        }
    }

private static void addRoundKey(int[][] state, int[] roundKey) {
    for (int i = 0; i < Nb; i++) {
        for (int j = 0; j < Nb; j++) {
            state[j][i] ^= (roundKey[i] >>> (8 * (3 - j))) & 0xFF;
        }
    }
}

// Conversions between state and byte arrays
private static int[][] toState(byte[] input) {
    int[][] state = new int[4][4];
    for (int i = 0; i < 16; i++) {
        state[i % 4][i / 4] = input[i] & 0xFF;
    }
    return state;
}

private static byte[] fromState(int[][] state) {
    byte[] output = new byte[16];
    for (int i = 0; i < 16; i++) {
        output[i] = (byte) state[i % 4][i / 4];
    }
    return output;
}



public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Input plaintext and pad to 16-byte boundary
    System.out.print("Enter the plaintext: ");
    String plainText = scanner.nextLine();
    while (plainText.length() % 16 != 0) {
        plainText += ' ';  // Pad the plaintext to a multiple of 16 bytes
    }

    // Input key and validate length
    System.out.print("Enter the key (length == 16 characters == 128 bits)\n>");
    String key = scanner.nextLine();
    while (key.length() != 16) {
        System.out.print("Please make sure you have the adequate key length (16 characters == 128 bits)\n>");
        key = scanner.nextLine();
    }

    // Convert key to byte array
    byte[] keyBytes = key.getBytes();

    // Encrypt the plaintext block by block
    byte[] cipherTextBytes = new byte[plainText.length()];
    for (int i = 0; i < plainText.length(); i += 16) {
        byte[] block = plainText.substring(i, Math.min(i + 16, plainText.length())).getBytes();
        byte[] encryptedBlock = encrypt(block, keyBytes);
        System.arraycopy(encryptedBlock, 0, cipherTextBytes, i, encryptedBlock.length);
    }

    // Display the ciphertext in hexadecimal format
    System.out.println("Ciphertext (in hex):");
    for (byte b : cipherTextBytes) {
        System.out.print(String.format("%02X ", b));
    }
    System.out.println();

    // Decrypt the ciphertext block by block
    byte[] decryptedTextBytes = new byte[cipherTextBytes.length];
    for (int i = 0; i < cipherTextBytes.length; i += 16) {
        byte[] block = Arrays.copyOfRange(cipherTextBytes, i, Math.min(i + 16, cipherTextBytes.length));
        byte[] decryptedBlock = decrypt(block, keyBytes);
        System.arraycopy(decryptedBlock, 0, decryptedTextBytes, i, decryptedBlock.length);
    }

    // Convert decrypted text to string and display
    String decryptedText = new String(decryptedTextBytes).trim(); // Trim padding spaces
    System.out.println("Decrypted text:");
    System.out.println(decryptedText);
}



}