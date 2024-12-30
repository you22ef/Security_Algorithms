import java.util.HashMap;
import java.util.HexFormat;
import java.util.Scanner;

public class DES {
    
    // Hexadecimal to binary conversion
    public static String hex2bin(String s) {
        HashMap<Character, String> mp = new HashMap<>();
        mp.put('0', "0000");
        mp.put('1', "0001");
        mp.put('2', "0010");
        mp.put('3', "0011");
        mp.put('4', "0100");
        mp.put('5', "0101");
        mp.put('6', "0110");
        mp.put('7', "0111");
        mp.put('8', "1000");
        mp.put('9', "1001");
        mp.put('A', "1010");
        mp.put('B', "1011");
        mp.put('C', "1100");
        mp.put('D', "1101");
        mp.put('E', "1110");
        mp.put('F', "1111");
        
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            bin.append(mp.get(s.charAt(i)));
        }
        return bin.toString();
    }

    // Binary to hexadecimal conversion
    public static String bin2hex(String s) {
        HashMap<String, Character> mp = new HashMap<>();
        mp.put("0000", '0');
        mp.put("0001", '1');
        mp.put("0010", '2');
        mp.put("0011", '3');
        mp.put("0100", '4');
        mp.put("0101", '5');
        mp.put("0110", '6');
        mp.put("0111", '7');
        mp.put("1000", '8');
        mp.put("1001", '9');
        mp.put("1010", 'A');
        mp.put("1011", 'B');
        mp.put("1100", 'C');
        mp.put("1101", 'D');
        mp.put("1110", 'E');
        mp.put("1111", 'F');
        
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < s.length(); i += 4) {
            String ch = s.substring(i, i + 4);
            hex.append(mp.get(ch));
        }
        return hex.toString();
    }

    // Binary to decimal conversion
    public static int bin2dec(int binary) {
        int decimal = 0, i = 0;
        while (binary != 0) {
            int dec = binary % 10;
            decimal += dec * Math.pow(2, i);
            binary /= 10;
            i++;
        }
        return decimal;
    }

    // Decimal to binary conversion
    public static String dec2bin(int num) {
        String res = Integer.toBinaryString(num);
        while (res.length() % 4 != 0) {
            res = '0' + res;
        }
        return res;
    }

    // Permute function to rearrange the bits
    public static String permute(String k, int[] arr, int n) {
        StringBuilder permutation = new StringBuilder();
        for (int i = 0; i < n; i++) {
            permutation.append(k.charAt(arr[i] - 1));
        }
        return permutation.toString();
    }

    // Shifting the bits towards left by nth shifts
    public static String shift_left(String k, int nth_shifts) {
        for (int i = 0; i < nth_shifts; i++) {
            k = k.substring(1) + k.charAt(0);
        }
        return k;
    }

    // Calculating XOR of two strings of binary number a and b
    public static String xor(String a, String b) {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            ans.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return ans.toString();
    }

    // Initial Permutation Table
    static int[] initial_perm = {58, 50, 42, 34, 26, 18, 10, 2,
                                  60, 52, 44, 36, 28, 20, 12, 4,
                                  62, 54, 46, 38, 30, 22, 14, 6,
                                  64, 56, 48, 40, 32, 24, 16, 8,
                                  57, 49, 41, 33, 25, 17, 9, 1,
                                  59, 51, 43, 35, 27, 19, 11, 3,
                                  61, 53, 45, 37, 29, 21, 13, 5,
                                  63, 55, 47, 39, 31, 23, 15, 7};

    // Expansion D-box Table
    static int[] exp_d = {32, 1, 2, 3, 4, 5, 4, 5,
                           6, 7, 8, 9, 8, 9, 10, 11,
                           12, 13, 12, 13, 14, 15, 16, 17,
                           16, 17, 18, 19, 20, 21, 20, 21,
                           22, 23, 24, 25, 24, 25, 26, 27,
                           28, 29, 28, 29, 30, 31, 32, 1};

    // Straight Permutation Table
    static int[] per = {16, 7, 20, 21,
                         29, 12, 28, 17,
                         1, 15, 23, 26,
                         5, 18, 31, 10,
                         2, 8, 24, 14,
                         32, 27, 3, 9,
                         19, 13, 30, 6,
                         22, 11, 4, 25};

    // S-box Table
    static int[][][] sbox = {
        {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
         {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
         {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
         {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
        
        {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
         {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
         {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
         {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
        
        {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
         {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
         {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
         {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
        
        {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
         {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
         {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
         {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
        
        {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
         {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
         {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
         {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
        
        {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
         {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
         {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
         {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
        
        {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
         {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
         {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
         {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
        
        {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
         {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
         {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
         {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};

    // Final Permutation Table
    static int[] final_perm = {40, 8, 48, 16, 56, 24, 64, 32,
                                39, 7, 47, 15, 55, 23, 63, 31,
                                38, 6, 46, 14, 54, 22, 62, 30,
                                37, 5, 45, 13, 53, 21, 61, 29,
                                36, 4, 44, 12, 52, 20, 60, 28,
                                35, 3, 43, 11, 51, 19, 59, 27,
                                34, 2, 42, 10, 50, 18, 58, 26,
                                33, 1, 41, 9, 49, 17, 57, 25};

    public static String encrypt(String pt, String[] rkb, String[] rk) {
    // Convert the plaintext to binary
    pt = hex2bin(pt);

    // Padding plaintext to make it a multiple of 64 bits
    int paddingLength = (64 - pt.length() % 64) % 64;
    for (int i = 0; i < paddingLength; i++) {
        pt += "0";  // Padding with zero bits
    }

    // Split plaintext into 64-bit blocks and encrypt each block
    StringBuilder ciphertext = new StringBuilder();
    for (int blockStart = 0; blockStart < pt.length(); blockStart += 64) {
        String block = pt.substring(blockStart, blockStart + 64);

        // Initial Permutation
        block = permute(block, initial_perm, 64);
        System.out.println("After initial permutation: " + bin2hex(block));

        // Splitting into left and right
        String left = block.substring(0, 32);
        String right = block.substring(32, 64);

        for (int i = 0; i < 16; i++) {
            // Expansion D-box: Expanding the 32 bits data into 48 bits
            String right_expanded = permute(right, exp_d, 48);

            // XOR RoundKey[i] and right_expanded
            String xor_x = xor(right_expanded, rkb[i]);

            // S-boxes: substituting the value from s-box table
            StringBuilder sbox_str = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                int row = bin2dec(Integer.parseInt(xor_x.charAt(j * 6) + "" + xor_x.charAt(j * 6 + 5)));
                int col = bin2dec(Integer.parseInt(xor_x.substring(j * 6 + 1, j * 6 + 5)));
                int val = sbox[j][row][col];
                sbox_str.append(dec2bin(val));
            }

            // Straight D-box: After substituting, rearrange the bits
            sbox_str = new StringBuilder(permute(sbox_str.toString(), per, 32));

            // XOR left and sbox_str
            String result = xor(left, sbox_str.toString());
            left = result;

            // Swap left and right for the next round (except after the last round)
            if (i != 15) {
                String temp = left;
                left = right;
                right = temp;
            }
            System.out.println("Round " + (i + 1) + " " + bin2hex(left) + " " + bin2hex(right) + " " + rk[i]);
        }

        // Combine left and right
        String combined = left + right;

        // Final permutation to get ciphertext for this block
        String cipher_text = permute(combined, final_perm, 64);
        ciphertext.append(cipher_text);  // Add the block's ciphertext to the result
    }

    // Return the final ciphertext
    return ciphertext.toString();
}

    
    public static String stringToBinary(String input) {
        StringBuilder binaryString = new StringBuilder();
        for (char character : input.toCharArray()) {
            binaryString.append(String.format("%8s", Integer.toBinaryString(character)).replace(' ', '0'));
        }
        
        // Calculate the number of bits to pad
        int length = binaryString.length();
        int paddingLength = (4 - (length % 4)) % 4; // Calculate padding required to make the length a multiple of 4
        StringBuilder paddedBinaryString = new StringBuilder();
        
        // Pad with zeros if necessary
        for (int i = 0; i < paddingLength; i++) {
            paddedBinaryString.append('0');
        }
        
        // Append the original binary string
        paddedBinaryString.append(binaryString.toString());

        return paddedBinaryString.toString();
    }

    public static String binaryToString(String binary) {
        StringBuilder string = new StringBuilder();
        
        // Process every 8 bits as a character (1 byte)
        for (int i = 0; i < binary.length(); i += 8) {
            String byteString = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteString, 2); // Convert binary to integer
            string.append((char) charCode); // Convert integer to character and append to result
        }
        
        return string.toString();
    }
    public static String toHex(String value) {
        return HexFormat.of().formatHex(value.getBytes()).toUpperCase();
    }
    public static String fromHex(String value) {
        return new String(HexFormat.of().parseHex(value));
    }
    public static void main(String[] args) {

        // String pt = "123456ABCD132536";
        // String key = "AABB09182736CCDD";
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for input
        System.out.print("Enter the Plain text: ");
        String pt = scanner.nextLine();
        pt = toHex(pt);
        System.out.print("Enter the key: ");
        String key = scanner.nextLine();
        while(key.length()!=8)
        {
            System.out.println("Key length should be 8 characters");
            System.out.print("Enter the key: ");
            key = scanner.nextLine();
        }
        key = toHex(key);
        
        

        // Key generation
        // --hex to binary
        key = hex2bin(key);

        // --parity bit drop table
        int[] keyp = {57, 49, 41, 33, 25, 17, 9,
                       1, 58, 50, 42, 34, 26, 18,
                       10, 2, 59, 51, 43, 35, 27,
                       19, 11, 3, 60, 52, 44, 36,
                       63, 55, 47, 39, 31, 23, 15,
                       7, 62, 54, 46, 38, 30, 22,
                       14, 6, 61, 53, 45, 37, 29,
                       21, 13, 5, 28, 20, 12, 4};

        // getting 56 bit key from 64 bit using the parity bits
        key = permute(key, keyp, 56);

        // Number of bit shifts
        int[] shift_table = {1, 1, 2, 2,
                             2, 2, 2, 2,
                             1, 2, 2, 2,
                             2, 2, 2, 1};

        // Key- Compression Table : Compression of key from 56 bits to 48 bits
        int[] key_comp = {14, 17, 11, 24, 1, 5,
                          3, 28, 15, 6, 21, 10,
                          23, 19, 12, 4, 26, 8,
                          16, 7, 27, 20, 13, 2,
                          41, 52, 31, 37, 47, 55,
                          30, 40, 51, 45, 33, 48,
                          44, 49, 39, 56, 34, 53,
                          46, 42, 50, 36, 29, 32};

        // Splitting
        String left = key.substring(0, 28); 
        String right = key.substring(28, 56); 

        String[] rkb = new String[16];// rkb for RoundKeys in binary
        String[] rk = new String[16];// rk for RoundKeys in hexadecimal
        for (int i = 0; i < 16; i++) {
            // Shifting the bits by nth shifts by checking from shift table
            left = shift_left(left, shift_table[i]);
            right = shift_left(right, shift_table[i]);

            // Combination of left and right string
            String combine_str = left + right;

            // Compression of key from 56 to 48 bits
            String round_key = permute(combine_str, key_comp, 48);

            rkb[i] = round_key;
            rk[i] = bin2hex(round_key);
        }

        System.out.println("Encryption");
        String cipher_text = bin2hex(encrypt(pt, rkb, rk));
        System.out.println("Cipher Text : " + cipher_text);

        System.out.println("Decryption");
        String[] rkb_rev = new String[16];
        String[] rk_rev = new String[16];
        for (int i = 0; i < 16; i++) {
            rkb_rev[i] = rkb[15 - i];
            rk_rev[i] = rk[15 - i];
        }
        String text = bin2hex(encrypt(cipher_text, rkb_rev, rk_rev));
        System.out.println("Plain Text : " + text);
        System.out.println("Plain Text : " + fromHex(text));

    }
}

