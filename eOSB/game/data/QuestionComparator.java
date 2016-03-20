package eOSB.game.data;

import java.util.Comparator;

import eOSB.game.controller.Question;

public class QuestionComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof Question && o2 instanceof Question) {
			Question q1 = (Question) o1;
			Question q2 = (Question) o2;
			
			String num1String = q1.getNumber();
			boolean isQ1Bonus = false;
			if (num1String.contains("b")) {
				num1String = num1String.substring(0, num1String.length() - 1);
				isQ1Bonus = true;
			}
			int num1Int = Integer.parseInt(num1String);
			
			String num2String = q2.getNumber();
			boolean isQ2Bonus = false;
			if (num2String.contains("b")) {
				num2String = num2String.substring(0, num2String.length() - 1);
				isQ2Bonus = true;
			}
			int num2Int = Integer.parseInt(num2String);
			
			
			if (num1Int == num2Int) {
				if (isQ1Bonus) {
					return 1;
				}
				else {
					return -1;
				}
			}
			else {
				if (num1Int < num2Int) {
					return -1;
				}
				else 
					return 1;
			}
		}
		return 0;
	}

}
