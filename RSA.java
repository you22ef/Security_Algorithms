import java.util.*;

public class RSA {
    // Set to store prime numbers
    private static int publicKey; // Public key (e)
    private static int privateKey; // Private key (d)
    private static int n; // Modulus (n = p * q)
    private static int p; // First prime number
    private static int q; // Second prime number
    private static Random random = new Random();

    public static void main(String[] args) {
        // Step 3: Key generation process
        // int num = modularExponentiation(13, 3, 15);
        // System.out.println(num);
        keyGeneration();

        // Display generated keys and primes
        System.out.println("Generated Keys:");
        System.out.println("Public Key (e): " + publicKey);
        System.out.println("Private Key (d): " + privateKey);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + n);

        // Original message to be encrypted and decrypted
        String message = "Test Message";

        // Step 1: Encrypt the message
        List<Integer> encoded = encodeMessage(message);

        System.out.println("\nOriginal Message: " + message);
        System.out.println("Encoded Message: " + encoded);

        // Step 6: Decrypt the encoded message using CRT
        String decoded = decodeMessage(encoded);
        System.out.println("Decoded Message: " + decoded);
    }

    // Fermat's primality test to check if a number is prime
    private static boolean isPrimeFermat(int number, int iterations) {
        if (number <= 1) return false;
        if (number <= 3) return true;

        for (int i = 0; i < iterations; i++) {
            int a = 2 + random.nextInt(number - 3); // Random integer in range [2, number-2]
            if (modularExponentiation(a, number - 1, number) != 1) {
                return false;
            }
        }
        return true;
    }

    // Step 3.1: Pick a prime number using Fermat's primality test
    private static int pickRandomPrime() {
        while (true) {
            int candidate = 50 + random.nextInt(200); // Generate a random number in range [50, 250]
            if (isPrimeFermat(candidate, 5)) {
                return candidate;
            }
        }
    }

    // Step 3: Generate keys for RSA
    private static void keyGeneration() {
        // Step 3.1: Select two distinct prime numbers p and q using Fermat's method
        p = pickRandomPrime();
        q = pickRandomPrime();
        while (p == q) {
            q = pickRandomPrime();
        }

        // Step 3.2: Compute n = p * q
        n = p * q;

        // Step 3.3: Compute ϕ(n) = (p - 1) * (q - 1)
        int phi = (p - 1) * (q - 1);

        // Step 3.4: Select public key e such that 1 < e < ϕ(n) and gcd(e, ϕ(n)) = 1
        publicKey = 2;
        while (gcd(publicKey, phi) != 1) {
            publicKey++;
        }

        // Step 3.5: Compute private key d using the modular multiplicative inverse of e mod ϕ(n)
        privateKey = modInverse(publicKey, phi);
    }

    // Helper function: Compute the greatest common divisor (GCD) of two numbers
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Helper function: Compute modular multiplicative inverse of a under modulo m
    private static int modInverse(int a, int m) {
        int r0 = m, r1 = a;  // Initialize remainders
        int s0 = 1, s1 = 0;  // Coefficients for the first number (m)
        int t0 = 0, t1 = 1;  // Coefficients for the second number (a)
    
        while (r1 != 0) {
            int q = r0 / r1;  // Quotient
    
            // Update remainders
            int r = r0 - q * r1;
            r0 = r1;
            r1 = r;
    
            // Update coefficients for s and t
            int s = s0 - q * s1;
            s0 = s1;
            s1 = s;
    
            int t = t0 - q * t1;
            t0 = t1;
            t1 = t;
        }
    
        // If r0 is not 1, the inverse does not exist
        if (r0 > 1) {
            return -1;  // No modular inverse exists
        }
    
        // Ensure the result is positive
        return t0 < 0 ? t0 + m : t0;  // Return the modular inverse (t0)
    }
    

    // Step 2: Efficient modular exponentiation using Square and Multiply algorithm
    private static int modularExponentiation(int base, int exponent, int modulus) {
        if (modulus == 1) return 0; // Handle edge case for modulus 1
    
         // Initialize result as 1
        base = base % modulus; // Ensure base is within the modulus range
        int result = base;
        // Find the most significant bit position in the exponent
        int t = Integer.highestOneBit(exponent)>>1;

        // Iterate from the most significant bit to the least significant bit
        while (t > 0) {
            result = (result * result) % modulus; // Square the result for every bit
            
            // If the current bit in the exponent is 1
            if ((exponent & t) != 0) {
                result = (result * base) % modulus;
            }
            
            t >>= 1; // Move to the next bit
        }
    
        return result;
    }
    

    // Step 1: Encrypt a single character using public key
    private static int encrypt(int message) {
        return modularExponentiation(message, publicKey, n);
    }

    // Step 6: Decrypt a single character using CRT
    private static int decryptCRT(int cipherText) {
        // Step 6.1: Compute dp = p mod (p - 1) and dq = d mod (q - 1) (Transforming)
        int dp = privateKey % (p - 1);
        int dq = privateKey % (q - 1);

        // Step 6.2: Compute mp = cipherText^dp mod p and mq = cipherText^dq mod q (Exponentiation)
        int mp = modularExponentiation(cipherText, dp, p);
        int mq = modularExponentiation(cipherText, dq, q);

        // Step 6.3: Combine results using CRT: m = (mp * q * qInv + mq * p * pInv) mod n (Inverse Transformation)
        int qInv = modInverse(q, p); // Modular inverse of q mod p
        int pInv = modInverse(p, q); // Modular inverse of p mod q

        int m = (mp * q * qInv + mq * p * pInv) % n;
        return m;
    }

    // Helper function: Encode an entire message
    private static List<Integer> encodeMessage(String message) {
        List<Integer> encoded = new ArrayList<>();
        for (char c : message.toCharArray()) {
            encoded.add(encrypt((int) c));
        }
        return encoded;
    }

    // Helper function: Decode an entire message
    private static String decodeMessage(List<Integer> encoded) {
        StringBuilder decoded = new StringBuilder();
        for (int code : encoded) {
            decoded.append((char) decryptCRT(code));
        }
        return decoded.toString();
    }
}
