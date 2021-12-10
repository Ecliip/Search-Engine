package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static String[] menuItems =  {
        "=== Menu ===",
                "1. Find a person",
                "2. Print all people",
                "0. Exit"
    };
    public static void main(String[] args) {
        while (true) {

            String filePath = "data.txt";
            File docToRead = new File(filePath);
            Scanner docScan = createDocScanner(docToRead);
            iterateMenu();
            Scanner scan = new Scanner(System.in);
            String usrStr = scan.nextLine();
            Map<String, List<Integer>> result;
            if ("1".equals(usrStr)) {
                System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
                String strategyString = scan.nextLine().toUpperCase();
                System.out.println("\nEnter a name or email to search all suitable people.");
                usrStr = scan.nextLine();

                Searcher searcher = null;
                if ("ALL".equalsIgnoreCase(strategyString)) {
                    searcher = new Searcher(new SearchAllMatches());
                } else if ("ANY".equalsIgnoreCase(strategyString)) {
                    searcher = new Searcher(new SearchAnyMatches());
                } else if ("NONE".equalsIgnoreCase(strategyString)) {
                    searcher = new Searcher(new SearchNoneMatches());
                }
                result = searcher.search(usrStr, docScan);
                printDataByLineNumber(result, filePath, usrStr); // test
            } else if ("2".equals(usrStr)) {
                printAllPersons(docScan);
            } else if ("0".equals(usrStr)) {
                docScan.close();
                break;
            }
            docScan.close();
        }
    }

    private static void printDataByLineNumber(Map<String, List<Integer>> result, String docToRead, String usrStr) {
        int size = result.get(usrStr).size();
        if (size > 0) {
            System.out.println(size + " persons found:");
            for (int i : result.get(usrStr)) {
                try {
                    var line = Files.readAllLines(Paths.get(docToRead)).get(i);
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No matching people found.");
        }
    }

    private static void printAllPersons(Scanner scanner) {
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
    }

    public static void iterateMenu() {
        for (String mItem : menuItems) {
            System.out.println(mItem);
        }
    }

    public static Scanner createDocScanner(File aFile) {
        Scanner scanner;
        try {
            scanner = new Scanner(aFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            scanner = null;
        }
        return scanner;
    }
}

interface SearchStrategy {
    Map<String, List<Integer>> search(String query, Scanner scanner);
}

class Searcher {
    private SearchStrategy strategy;
    private Scanner scanner;

    public Searcher(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public  Map<String, List<Integer>> search(String query, Scanner scanner) {
        return strategy.search(query, scanner);
    }
}

class SearchAllMatches implements SearchStrategy {

    @Override
    public Map<String, List<Integer>> search(String query, Scanner scanner) {
        int index = 0;
        Map<String, List<Integer>> map = new HashMap<>();
        String[] queryArr = query.split("\\s+");
        Set<String> uniqueQueryWords = new HashSet<>();
        for (String queryWord : queryArr) {
            uniqueQueryWords.add(queryWord.toLowerCase(Locale.ROOT));
        }
        int matchesNeeded = uniqueQueryWords.size();

        while (scanner.hasNextLine()) {
            int matchCounter = 0;
            String line = scanner.nextLine();
            map.putIfAbsent(query, new ArrayList<>());
            String[] lineWords = line.toLowerCase(Locale.ROOT).split("\\s+");
            Set<String> uniqueWordsFromLine = new HashSet<>();
            for (String wordFromLine : lineWords) {
                uniqueWordsFromLine.add(wordFromLine.toLowerCase(Locale.ROOT));
            }

            for (String word : uniqueWordsFromLine) {
                for (String queryWord : uniqueQueryWords) {
                    if (queryWord.equalsIgnoreCase(word)) {
                        matchCounter++;
                        continue;
                    }
                }
                if (matchCounter == matchesNeeded) {
                    map.get(query).add(index);
                    break;
                }
            }

            index++;
        }
        scanner.close();
        return map;
    }
}

class SearchAnyMatches implements SearchStrategy {

    @Override
    public Map<String, List<Integer>> search(String query, Scanner scanner) {
        int index = 0;
        Map<String, List<Integer>> map = new HashMap<>();
        String[] queryArr = query.split("\\s+");
        Set<String> uniqueQueryWords = new HashSet<>();
        for (String queryWord : queryArr) {
            uniqueQueryWords.add(queryWord.toLowerCase(Locale.ROOT));
        }
        int matchesNeeded = uniqueQueryWords.size();

        while (scanner.hasNextLine()) {
            int matchCounter = 0;
            String line = scanner.nextLine();
            map.putIfAbsent(query, new ArrayList<>());
            String[] lineWords = line.toLowerCase(Locale.ROOT).split("\\s+");
            Set<String> uniqueWordsFromLine = new HashSet<>();
            for (String wordFromLine : lineWords) {
                uniqueWordsFromLine.add(wordFromLine.toLowerCase(Locale.ROOT));
            }

            for (String word : uniqueWordsFromLine) {
                for (String queryWord : uniqueQueryWords) {
                    if (queryWord.equalsIgnoreCase(word)) {
                        matchCounter++;
                        break;
                    }
                }
                if (matchCounter > 0) {
                    map.get(query).add(index);
                    break;
                };
            }
            index++;
        }
        scanner.close();
        return map;
    }
}

class SearchNoneMatches implements SearchStrategy {

    @Override
    public Map<String, List<Integer>> search(String query, Scanner scanner) {
        {
            int index = 0;
            Map<String, List<Integer>> map = new HashMap<>();
            String[] queryArr = query.split("\\s+");
            Set<String> uniqueQueryWords = new HashSet<>();
            for (String queryWord : queryArr) {
                uniqueQueryWords.add(queryWord.toLowerCase(Locale.ROOT));
            }
            int matchesNeeded = uniqueQueryWords.size();

            while (scanner.hasNextLine()) {
                int matchCounter = 0;
                String line = scanner.nextLine();
                map.putIfAbsent(query, new ArrayList<>());
                String[] lineWords = line.toLowerCase(Locale.ROOT).split("\\s+");
                Set<String> uniqueWordsFromLine = new HashSet<>();
                for (String wordFromLine : lineWords) {
                    uniqueWordsFromLine.add(wordFromLine.toLowerCase(Locale.ROOT));
                }

                for (String word : uniqueWordsFromLine) {
                    for (String queryWord : uniqueQueryWords) {
                        if (queryWord.equalsIgnoreCase(word)) {
                            matchCounter++;
                            continue;
                        }
                    }
                }
                if (matchCounter <= 0) {
                    map.get(query).add(index);
                };
                matchCounter = 0;
                index++;
            }
            scanner.close();
            return map;
        }
    }
}
