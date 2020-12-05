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
		return null;
	}
}
