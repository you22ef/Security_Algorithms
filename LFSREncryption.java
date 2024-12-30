import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class LFSREncryption {

    private int[] lfsr;
    private final int[] seed; // Store the initial seed to reset the LFSR
    private final List<Integer> taps; // List of tap positions

    public LFSREncryption(int[] seed, List<Integer> taps) {
        this.seed = seed.clone(); // Store initial seed
        this.lfsr = seed.clone(); // Initialize LFSR with the seed
        this.taps = taps; // List of tap bits for feedback
    }

    // Generate the next bit in the LFSR sequence
    public int nextBit() {
        int newBit = 0;
        // XOR the bits at each tap position
        for (int tap : taps) 
        {
            newBit ^= lfsr[tap];
        }
        int b = lfsr[0];
        System.arraycopy(lfsr, 1, lfsr, 0, lfsr.length - 1); // Shift left
        lfsr[lfsr.length - 1] = newBit; // Set the new bit at the end
        return b;
    }

    // Generate keystream of a specific length
    public List<Integer> generateKeystream(int length) 
    {
        List<Integer> keystream = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            keystream.add(nextBit());
        }
        return keystream;
    }

    // Reset LFSR to the initial seed
    public void resetLFSR() {
        this.lfsr = seed.clone();
    }

    // Encrypt or decrypt message using LFSR keystream
    public String process(String message) {
        resetLFSR(); // Reset LFSR to ensure the same keystream is used
        byte[] messageBytes = message.getBytes();
        List<Integer> keystream = generateKeystream(messageBytes.length * 8);

        byte[] processedBytes = new byte[messageBytes.length];
        for (int i = 0; i < messageBytes.length; i++) {
            byte processedByte = 0;
            for (int j = 0; j < 8; j++)
            {
                processedByte <<= 1;
                processedByte |= (keystream.get(i * 8 + j) ^ ((messageBytes[i] >> (7 - j)) & 1));
            }
            processedBytes[i] = processedByte;
        }
        return new String(processedBytes);
    }


    // Display table of cycles (for demonstration)
    public void displayCycleTable(int cycles) {
        int[] tempLfsr = seed.clone(); // Temporary LFSR to simulate cycles
        System.out.print("clk   ");
        for (int i = 0; i < tempLfsr.length; i++) {
            System.out.printf("FF%d   ", i);
        }
        System.out.println("Output");

        for (int i = 0; i < cycles; i++) {
            System.out.printf("%-5d  ", i);
            for (int j = 0; j < tempLfsr.length; j++) 
            {
                System.out.printf("%-5d ", tempLfsr[j]);
            }
            System.out.printf("%-5d\n", tempLfsr[0]);

            // Calculate the new bit using multiple taps for feedback
            int newBit = 0;
            for (int tap : taps) {
                newBit ^= tempLfsr[tap];
            }

            // Shift left and insert the new bit at the end
            System.arraycopy(tempLfsr, 1, tempLfsr, 0, tempLfsr.length - 1);
            tempLfsr[tempLfsr.length - 1] = newBit;

            // Break if we detect a cycle repeat (only needed if we want to detect the period)
            boolean cycleDetected = true;
            for (int j = 0; j < seed.length; j++) {
                if (tempLfsr[j] != seed[j]) 
                {
                    cycleDetected = false;
                    break;
                }
            }
            if (i > 0 && cycleDetected) break;
        }
    }

    // Check if LFSR is primitive, irreducible, or reducible
    public String checkLFSRType() {
        resetLFSR();
        int maxPeriod = (int) Math.pow(2, seed.length) - 1;
        Set<String> states = new HashSet<>();

        int count = 0;

        for (int i = 0; i < maxPeriod; i++) {
            String state = getCurrentState();
            if (states.contains(state)) {
                count++;
                return "Reducible"; // Cycle detected before max period, hence reducible
            }
            states.add(state);
            nextBit();
        }

        // If it reaches max period and repeats, it is primitive, otherwise irreducible
        if (states.size() == maxPeriod) {
            return "Primitive";
        } else {
            return "Irreducible";
        }
    }

    // Helper to get current LFSR state as a String
    private String getCurrentState() {
        StringBuilder state = new StringBuilder();
        for (int bit : lfsr) {
            state.append(bit);
        }
        return state.toString();
    }
    public static LFSREncryption fromPolynomialEquation(String equation) {
        // Extract powers from the equation
        String[] terms = equation.split("\\+");
        int maxPower = 0;
        boolean first = false;
        List<Integer> tapPositions = new ArrayList<>();
    
        for (String term : terms) {
            term = term.trim();
            if (term.equals("1")) {
                tapPositions.add(0);
            } else if (term.startsWith("x^")) {
                int power = Integer.parseInt(term.substring(2));
                if (power > maxPower) {
                    maxPower = power;
                }
                if(first)
                {
                    tapPositions.add(power);
                }
                first = true;
            } else if (term.equals("x")) {
                if(first)
                {
                    tapPositions.add(1);
                }
                first = true;
                if (1 > maxPower) {
                    maxPower = 1;
                }
            }
        }
    
        // Ask user to input the seed
        Scanner scanner = new Scanner(System.in);
        int[] seed = new int[maxPower];
        System.out.println("Enter the seed (0 or 1) for each flip-flop:");
        for (int i = 0; i < maxPower; i++) { // Use i <= maxPower to fill all positions
            System.out.print("Seed for position " + i + ": ");
            seed[i] = scanner.nextInt();
        }
    
        // Print the seed and tap positions
        System.out.println("\nSeed: ");
        for (int bit : seed) {
            System.out.print(bit + " ");
        }
        System.out.println("\nTap Positions: " + tapPositions);
    
        return new LFSREncryption(seed, tapPositions);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get the polynomial equation from the user
        System.out.print("Enter the polynomial equation (e.g., x^3 + x + 1): ");
        String equation = scanner.nextLine();
        
        // Create LFSR instance based on equation
        LFSREncryption lfsr = LFSREncryption.fromPolynomialEquation(equation);

        System.out.print("Enter the Message : ");
        String message = scanner.nextLine();; // Message to encrypt
        System.out.println("Original Message: " + message);

        // Encrypt the message
        String encryptedMessage = lfsr.process(message);
        System.out.println("Encrypted Message: " + encryptedMessage);

        // Decrypt the message
        String decryptedMessage = lfsr.process(encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);

        // Display cycle table
        System.out.println("\nCycle Table:");
        lfsr.displayCycleTable((int) Math.pow(2, lfsr.seed.length) - 1); // Display cycles up to 2^m - 1

        // Check LFSR type
        String lfsrType = lfsr.checkLFSRType();
        System.out.println("\nLFSR Type: " + lfsrType);
    }
}
