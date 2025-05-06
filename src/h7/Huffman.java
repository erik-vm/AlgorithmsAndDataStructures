package h7;

import java.util.*;

/**
 * Prefix codes and Huffman tree.
 * Tree depends on source data.
 */
public class Huffman {

    private static class Node implements Comparable<Node> {
        private final Byte character;
        private final int frequency;
        private final Node left, right;

        byte code;
        int codeLength;

        Node(Byte character, int frequency, Node left, Node right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return (left == null) && (right == null);
        }

        public int compareTo(Node anotherNode) {
            int fDiff = frequency - anotherNode.frequency;
            if (fDiff != 0) {
                return fDiff;
            }
            return this.hashCode() - anotherNode.hashCode();
        }

        public void setCode(byte code, int codeLength) {
            this.code = code;
            this.codeLength = codeLength;
        }
    }

    private final Map<Byte, Node> huffmanTable = new HashMap<>();
    private int length = 0;
    private boolean isEmpty = false;

    /**
     * Constructor to build the Huffman code for a given byte array.
     * @param original source data
     */
    public Huffman(byte[] original) {
        // Handle empty array case
        if (original.length == 0) {
            isEmpty = true;
            return;
        }

        // Count frequencies of each byte
        int[] frequencies = new int[256];
        for (byte b : original) {
            frequencies[b & 0xFF]++;
        }

        // Create nodes for each character with non-zero frequency
        TreeSet<Node> nodes = new TreeSet<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                nodes.add(new Node((byte) i, frequencies[i], null, null));
            }
        }

        // Special case: Single unique character
        if (nodes.size() == 1) {
            Node singleNode = nodes.first();
            singleNode.setCode((byte) 0, 1);
            huffmanTable.put(singleNode.character, singleNode);
            length = original.length; // One bit per character
            return;
        }

        // Build the Huffman tree
        while (nodes.size() > 1) {
            Node first = nodes.pollFirst();
            Node second = nodes.pollFirst();
            nodes.add(new Node(null, first.frequency + second.frequency, first, second));
        }

        // Generate codes for each character
        if (!nodes.isEmpty()) {
            Node root = nodes.first();
            generateMap(root, (byte) 0, 0);
            // Uncomment for debugging
            // printTree(root, 0);
        }
    }

    private void generateMap(Node node, byte code, int codeLength) {
        if (node.isLeaf()) {
            if (codeLength < 1) {
                codeLength = 1;
            }
            length += node.frequency * codeLength;
            node.setCode(code, codeLength);
            huffmanTable.put(node.character, node);
        } else {
            if (node.left != null) {
                generateMap(node.left, (byte) (code << 1), codeLength + 1);
            }
            if (node.right != null) {
                generateMap(node.right, (byte) ((code << 1) + 1), codeLength + 1);
            }
        }
    }

    /**
     * Prints the Huffman tree (for debugging)
     */
    private void printTree(Node node, int depth) {
        // Create indentation for tree visualization
        String indent = new String(new char[depth * 2]).replace('\0', ' ');

        if (node.isLeaf()) {
            String codeBinary = Integer.toBinaryString(node.code);
            // Pad the binary representation to match code length
            StringBuilder padding = new StringBuilder();
            for (int i = 0; i < Math.max(0, node.codeLength - codeBinary.length()); i++) {
                padding.append('0');
            }
            codeBinary = padding.toString() + codeBinary;

            System.out.printf("%sLeaf: %s (byte: %d, freq: %d, code: %s)%n",
                    indent,
                    isPrintable(node.character) ? String.format("'%c'", node.character) : "",
                    node.character & 0xFF,
                    node.frequency,
                    codeBinary);
        } else {
            System.out.printf("%sNode: (freq: %d)%n", indent, node.frequency);
            if (node.left != null) {
                System.out.printf("%sLeft -> %n", indent);
                printTree(node.left, depth + 1);
            }
            if (node.right != null) {
                System.out.printf("%sRight -> %n", indent);
                printTree(node.right, depth + 1);
            }
        }
    }

    private boolean isPrintable(byte b) {
        return b >= 32 && b < 127;
    }

    /**
     * Length of encoded data in bits.
     * @return number of bits
     */
    public int bitLength() {
        return length;
    }

    /**
     * Encoding the byte array using this prefixcode.
     *
     * @param origData original data
     * @return encoded data
     */
    public byte[] encode(byte[] origData) {
        // Handle empty array case
        if (isEmpty || origData.length == 0) {
            return new byte[0];
        }

        int bitPointer = 0;
        byte[] result = new byte[(int) Math.ceil(length / 8.0)];

        for (byte b : origData) {
            Node node = huffmanTable.get(b);
            if (node == null) {
                throw new IllegalArgumentException("Character not found in Huffman table: " + b);
            }

            // Write each bit of the code to the result array
            for (int i = node.codeLength - 1; i >= 0; i--) {
                boolean bit = ((node.code >> i) & 1) == 1;
                if (bit) {
                    result[bitPointer / 8] |= (1 << (bitPointer % 8));
                }
                bitPointer++;
            }
        }

        return result;
    }

    /**
     * Decoding the byte array using this prefixcode.
     *
     * @param encodedData encoded data
     * @return decoded data (hopefully identical to original)
     */
    public byte[] decode(byte[] encodedData) {
        // Handle empty array case
        if (isEmpty || encodedData.length == 0) {
            return new byte[0];
        }

        List<Byte> result = new ArrayList<>();
        int bitPointer = 0;

        // Create a map from code to character for faster lookup
        Map<Integer, Byte> codeToCharMap = new HashMap<>();
        for (Map.Entry<Byte, Node> entry : huffmanTable.entrySet()) {
            Node node = entry.getValue();
            // Store code as integer for easier comparison
            int codeKey = (node.code & 0xFF) | (node.codeLength << 16);
            codeToCharMap.put(codeKey, entry.getKey());
        }

        int currentCode = 0;
        int currentCodeLength = 0;

        while (bitPointer < length) {
            // Read next bit
            boolean bit = (encodedData[bitPointer / 8] & (1 << (bitPointer % 8))) != 0;

            // Append bit to current code
            currentCode = (currentCode << 1) | (bit ? 1 : 0);
            currentCodeLength++;

            // Check if current code matches any character
            int codeKey = currentCode | (currentCodeLength << 16);
            Byte character = codeToCharMap.get(codeKey);

            if (character != null) {
                result.add(character);
                currentCode = 0;
                currentCodeLength = 0;
            }

            bitPointer++;
        }

        // Convert list to array
        byte[] output = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            output[i] = result.get(i);
        }
        return output;
    }

    /**
     * Main method.
     */
    public static void main(String[] params) {
        // Test with the example data
        String text = "AAAAAAAAAAAAABBBBBBCCCDDEEF";
        byte[] orig = text.getBytes();
        Huffman huf = new Huffman(orig);
        byte[] encoded = huf.encode(orig);
        byte[] decoded = huf.decode(encoded);

        // must be equal: orig, decoded
        System.out.println("Original equals decoded: " + Arrays.equals(orig, decoded));
        int bitLength = huf.bitLength();
        System.out.println("Length of encoded data in bits: " + bitLength);

        // Additional tests

        // Test with empty array
        byte[] empty = new byte[0];
        Huffman emptyHuf = new Huffman(empty);
        byte[] emptyEncoded = emptyHuf.encode(empty);
        byte[] emptyDecoded = emptyHuf.decode(emptyEncoded);
        System.out.println("Empty array test: " + Arrays.equals(empty, emptyDecoded));

        // Test with array of length one
        byte[] single = {65}; // ASCII 'A'
        Huffman singleHuf = new Huffman(single);
        byte[] singleEncoded = singleHuf.encode(single);
        byte[] singleDecoded = singleHuf.decode(singleEncoded);
        System.out.println("Single byte test: " + Arrays.equals(single, singleDecoded));
        System.out.println("Single byte encoded length: " + singleHuf.bitLength());

        // Test with all unique bytes
        byte[] allUnique = {65, 66, 67, 68, 69}; // ASCII 'A', 'B', 'C', 'D', 'E'
        Huffman uniqueHuf = new Huffman(allUnique);
        byte[] uniqueEncoded = uniqueHuf.encode(allUnique);
        byte[] uniqueDecoded = uniqueHuf.decode(uniqueEncoded);
        System.out.println("All unique bytes test: " + Arrays.equals(allUnique, uniqueDecoded));

        // Calculate compression ratio for the example
        double originalBits = orig.length * 8;
        double compressedBits = bitLength;
        System.out.println("Original size (bits): " + originalBits);
        System.out.println("Compressed size (bits): " + compressedBits);
        System.out.println("Compression ratio: " + (originalBits / compressedBits));
    }
}