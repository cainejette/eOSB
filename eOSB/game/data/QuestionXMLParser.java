package eOSB.game.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eOSB.game.controller.Handler;
import eOSB.game.controller.Question;
import eOSB.game.controller.Question.Format;
import eOSB.game.controller.Question.Type;
import eOSB.game.controller.Round;

public class QuestionXMLParser {

	private Round round;
	private String token;
	
	public QuestionXMLParser(Round round, String token) {
		this.round = round;
		this.token = token;
		
		this.parseRound(token);
	}
	
	private void parseRound(String key) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(Handler.getDecryptedStream(key, new FileInputStream(this.round.getLocation())));
			Document doc = dBuilder.parse(Handler.getResourceAsStream(this.round.getLocation()));
			doc.getDocumentElement().normalize();
			List<Question> questions = new ArrayList<Question>();

			NodeList nList = doc.getElementsByTagName("Question");

			Question question = new Question();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					question = new Question();
					Element eElement = (Element) nNode;
					
					question.setNumber(getTagValue("QuestionPair", eElement));
					String type = getTagValue("QuestionType", eElement);
					if (type.equals("Toss-Up")) {
						question.setType(Type.TOSSUP);
					}
					else {
						question.setType(Type.BONUS);
						question.setNumber(question.getNumber() + "b");
					}

					// strip question subcategories. this was a new and unwanted change in 2012.
					String category = getTagValue("Category", eElement);
					if (category.contains("-")) {
						category = category.substring(0, category.indexOf("-") - 1);
					}
					question.setCategory(category);
					
					String format = getTagValue("QuestionFormat", eElement);
					if (format.equals("Multiple Choice")) {
						question.setFormat(Format.MULTIPLE_CHOICE);
					}
					else {
						question.setFormat(Format.SHORT_ANSWER);
					}
					
					question.setText(getTagValue("QuestionText", eElement));
					
					if (question.getFormat().equals(Format.MULTIPLE_CHOICE)) {
						question.addAnswerOption(getTagValue("AnswerW", eElement));
						question.addAnswerOption(getTagValue("AnswerX", eElement));
						question.addAnswerOption(getTagValue("AnswerY", eElement));
						question.addAnswerOption(getTagValue("AnswerZ", eElement));
					}
					
					question.addCorrectAnswer(getTagValue("CorrectAnswer", eElement));
					
				}
				questions.add(question);
//				System.out.println(question);
			}
			
			Collections.sort(questions, new QuestionComparator());
			System.out.println("SORTED:");
			for (Question question2 : questions) {
//				System.out.println(question2);
				this.round.addQuestion(question2);
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Round getRound() {
		return this.round;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
}