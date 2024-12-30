class GF28{
    static int add(int a, int b){
        return a ^ b;
    }
    // 00000000 00000101 -> i (x2 + 1) (x2 + x + 1)
    // 00000000 00000111 -> j
    // 00000000 00000000
    static int multiply(int a, int b){
        int[] solution = new int[2];
        solution[0] = 0;
        solution[1] = 0;
        for(int i = 0; i < 8;i++)
        {
            // to get bit ((var) >> bit) & 0x01
            if(((a>>i)&1) == 0){
                continue;
            }
            for(int j = 0;j<8;j++){
                if((i+j) >= 8){
                    solution[1] ^= (((b>>j)&1) << ((i+j-8)));
                }
                else{
                    solution[0] ^= (((b>>j)&1) << (i+j));
                }
            }
        }
        // P = x8 + x4 + x3 + x + 1
        // 00100101 10101000
        while (solution[1] != 0) {
            int highestBit = 31 - Integer.numberOfLeadingZeros(solution[1]); // Find the position of the highest set bit
            if (highestBit < 8) break; // No reduction needed if all bits are within 8
        
            int shift = highestBit - 8; // Determine how much to shift the irreducible polynomial
            int reductionPoly = (0b100011011 << shift); // Shifted P(x): 0b100011011 represents x^8 + x^4 + x^3 + x + 1
        
            // Perform XOR to reduce
            solution[1] ^= (reductionPoly >> 8); // Upper part of the shifted polynomial
            solution[0] ^= (reductionPoly & 0xFF); // Lower part of the shifted polynomial
        }
        
        return solution[0];
    }
    // public static void main(String[] args) {
    //     int a = 0b01101010;
    //     int b = 0b10001111;
    //     System.out.println(multiply(a, b));
    // }
}