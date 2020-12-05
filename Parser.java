import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;
import java.util.ArrayList;
import java.util.List;

public class Parser implements IParser {
	public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {
		//getting the lenght of the test string/word
		int n = w.length();

		if (n != 0) {
			//instentiating the max derivative steps to comply with CNF steps
			int numberOfSteps = ((2 * n) - 1);
			//creatin a list to store the rules and variables
			List<Word> listToCheck = new ArrayList<>();
			listToCheck.add(new Word(cfg.getRules().get(0).getVariable()));
			//iterating throught until the loop is less than the numberOfSteps
			for (int i = 0; i < numberOfSteps; i++) {
				List<Word> tempList = new ArrayList<>();
				for (Word tempWord : listToCheck) {
					int tempWordLength = tempWord.length();
					//if there are no variables boolien to establish break or continue
					boolean wordAllTerminal = wordAllTerminals(tempWord);
					//while we have variables
					if (!wordAllTerminal) {
						for (int j = 0; j < tempWordLength; j++) {
							//once we replaced all variables with terminasl
							if (tempWord.get(j).isTerminal()) {
								break;
							} else {
								//interating through all the rules within cfg
								for (Rule rule : cfg.getRules()) {
									if (tempWord.get(j).equals(rule.getVariable())) {
										if ((i < numberOfSteps / 2 && (!rule.getExpansion().isTerminal()))
												|| i >= numberOfSteps / 2 && (rule.getExpansion().isTerminal())) {
											int expansionLength = rule.getExpansion().length();
											List<Symbol> tempWordSymbolList = new ArrayList<>();
											for (int k = 0; k < j; k++) {
												tempWordSymbolList.add(tempWord.get(k));
											}
											for (int k = 0; k < expansionLength; k++) {
												tempWordSymbolList.add(rule.getExpansion().get(k));
											}
											for (int k = j + 1; k < tempWordLength; k++) {
												tempWordSymbolList.add(tempWord.get(k));
											}
											Symbol[] symbolArray = new Symbol[tempWordSymbolList.size()];
											for (int l = 0; l < tempWordSymbolList.size(); l++) {
												symbolArray[l] = tempWordSymbolList.get(l);
											}
											Word newWord = new Word(symbolArray);
											if (expansionLength != 0) {
												tempList.add(newWord);
											}
										}
									}
								}
							}
						}
					}
				}
				listToCheck = tempList;
				System.out.println("Derivation step " + (i+1) + ":" + tempList);
			}
			System.out.println("Possible combinations: " + listToCheck);
			List<Word> tempListToCheck = new ArrayList<>();
			for (Word wordToCheck : listToCheck) {
				if (wordAllTerminals(wordToCheck)) {
					tempListToCheck.add(wordToCheck);
				}
			}
			listToCheck = tempListToCheck;
			for (Word wordToCheck : listToCheck) {
				if (w.equals(wordToCheck)) {
					return true;
				}
			}
		} else {
			Variable start = cfg.getRules().get(0).getVariable();
			for (Rule rule : cfg.getRules()) {
				if (rule.getVariable().equals(start)) {
					if (rule.getExpansion().equals(new Word("")))
						System.out.println("Empty Match!");
					return true;
				}
			}
		}
		return false;
	}

	private boolean wordAllTerminals(Word tempWord) {
		int tempWordLength = tempWord.length();
		for (int j = 0; j < tempWordLength; j++) {
			if (!tempWord.get(j).isTerminal()) {
				return false;
			}
		}
		return true;
	}

	
	
	public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
		
		ParseTreeNode tree = 
		new ParseTreeNode(new Variable("A0"), 
		new ParseTreeNode(new Variable('Z'), 
		new ParseTreeNode(new Terminal('0'))),
		new ParseTreeNode(new Variable('B'), 
		new ParseTreeNode(new Variable('A'), 
		new ParseTreeNode(new Variable('Z'), 
		new ParseTreeNode(new Terminal('0'))), 
		new ParseTreeNode(new Variable('Y'), 
		new ParseTreeNode(new Terminal('1')))), 
		new ParseTreeNode(new Variable('Y'), 
		new ParseTreeNode(new Terminal('1')))));
		tree.print();
		
		///*
		int n = w.length();
    //int r = cfg.size();
    //Vector<String> startingsymbols = getVariable(cfg);
    String[] word = w.split("\\s");
    n = word.length;
    System.out.println("length of entry" + n);
    //let P[n,n,r] be an array of booleans. Initialize all elements of P to false.
    boolean P[][][] = initialize3DVector(n, n);
    //n-> number of words of string entrada, 
    //r-> number of nonterminal symbols

    //This grammar contains the subset Rs which is the set of start symbols
    for (int i = 1; i < n; i++) {
        for(int j = 0; j < n; j++) {
            String[] rule = (String[]) cfg.getVariable(j);
            if (rule.length == 2) {
                if (rule[1].equals(word[i])) {
                    System.out.println("entrou");
                    System.out.println(rule[1]);
                    P[i][1][j + 1] = true;
                }
            }
        }
    }
    for(int i = 2; i < n; i++) {
        System.out.println("FIRST:" + i);

        for(int j = 1; j < n - i + 1; j++) {
            System.out.println("SECOND:" + j);

            for(int k = 1; k < i - 1; k++) {
                System.out.println("THIRD:" + k);
                for(int g = 0; g < n; g++) {
                    String[] rule = (String[]) cfg.getVariable(g);
                    if (rule.length > 2) {
                        int A = returnPos(rule[0]);
                        int B = returnPos(rule[1]);
                        int C = returnPos(rule[2]);
                        System.out.println("A" + A);
                        System.out.println("B" + B);
                        System.out.println("C" + C);
                        if (A!=-1 && B!=-1 && C!=-1) {
                            if (P[j][k][B] && P[j + k][i - k][C]) {
                                System.out.println("entrou2");
                                P[j][i][A] = true;
                            }
                        }
                    }
                }
            }
        }
    }

    for(int x = 0; x < n; x++) {
        if(P[1][n][x]) return true;
    }
		//*/
		return null;
	}
}
