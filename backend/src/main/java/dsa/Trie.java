package dsa;

import java.util.HashMap;

public class Trie {

    // Node of the Trie
    private static class TrieNode {

        HashMap<Character, TrieNode> children;

        boolean isEndOfWord;

        TrieNode() {

            children = new HashMap<>();

            isEndOfWord = false;
        }
    }

    private TrieNode root;

    // Constructor
    public Trie() {

        root = new TrieNode();
    }

    // Insert a word into the Trie
    public void insert(String word) {

        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {

            char ch = word.charAt(i);

            if (!current.children.containsKey(ch)) {

                current.children.put(
                        ch,
                        new TrieNode());
            }

            current = current.children.get(ch);
        }

        current.isEndOfWord = true;
    }

    // Search for a complete word
    public boolean search(String word) {

        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {

            char ch = word.charAt(i);

            if (!current.children.containsKey(ch)) {

                return false;
            }

            current = current.children.get(ch);
        }

        return current.isEndOfWord;
    }

    // Check whether any word starts with a prefix
    public boolean startsWith(String prefix) {

        TrieNode current = root;

        for (int i = 0; i < prefix.length(); i++) {

            char ch = prefix.charAt(i);

            if (!current.children.containsKey(ch)) {

                return false;
            }

            current = current.children.get(ch);
        }

        return true;
    }
}