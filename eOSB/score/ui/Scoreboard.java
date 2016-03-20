package eOSB.score.ui;

import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

/**
 * The frame representing the scoreboard.
 * 
 * @author Caine Jette
 * 
 */
public class Scoreboard implements EventSubscriber<EventServiceEvent> {

  public void onEvent(EventServiceEvent arg0) {
    
  }
  
//  private JFrame frame;
//
//  private final JLabel teamALabel = new JLabel("  Team A  ");
//  private final JLabel teamBLabel = new JLabel("  Team B  ");
//  private final JLabel teamAScore = new JLabel("0");
//  private final JLabel teamBScore = new JLabel("0");
//
//  private final Color teamAColor = new Color(235, 114, 24);
//  private final Color teamBColor = new Color(150, 150, 255);
//
//  private final Font font = new Font("Courier", Font.BOLD, 100);
//
//  /**
//   * @param roundName the name of the round
//   */
//  public Scoreboard(String roundName) {
//    this.frame = new JFrame("electronic Scoreboard");
//    
//    this.initBoard();
//  }
//
//  /**
//   * @param roundName the name of the current round
//   * @param teamAName team A's name
//   * @param teamBName team B's name
//   * @param timePanel the panel containing the timing elements
//   */
//  public Scoreboard(String roundName, String teamAName, String teamBName, JPanel timePanel) {
//    this.frame = new JFrame("electronic Scoreboard");
//    this.teamALabel.setText(teamAName);
//    this.teamBLabel.setText(teamBName);
//    this.initFrame();
//    this.initBoard(timePanel);
//  }
//
//  /**
//   * Initializes the frame.
//   */
//  private void initFrame() {
//    this.frame.setIconImage(new ImageIcon(this.getClass().getResource(IconFactory.LOGO))
//        .getImage());
//    this.frame.setResizable(true);
//    this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//  }
//
//  /**
//   * Initializes the scoreboard
//   */
//  private void initBoard() {
//    this.initBoard(null);
//  }
//
//  /**
//   * Initializes the {@link Scoreboard}.
//   * @param timePanel the panel containing the timing elements
//   */
//  public void initBoard(JPanel timePanel) {
//    this.frame.dispose();
//
//    EventBus.subscribe(TeamNameEvent.class, this);
//    EventBus.subscribe(TeamScoreNumberEvent.class, this);
//
//    this.initComponents();
//
//    final JPanel panel = new JPanel();
//    final GroupLayout layout = new GroupLayout(panel);
//    panel.setLayout(layout);
//    panel.setBackground(Color.BLACK);
//
//    final ParallelGroup labelGroup = layout.createParallelGroup();
//    labelGroup.addComponent(this.teamALabel);
//    labelGroup.addComponent(this.teamBLabel);
//
//    final ParallelGroup scoreGroup = layout.createParallelGroup();
//    scoreGroup.addComponent(this.teamAScore);
//    scoreGroup.addComponent(this.teamBScore);
//
//    final ParallelGroup teamAGroup = layout.createParallelGroup();
//    teamAGroup.addComponent(this.teamALabel);
//    teamAGroup.addComponent(this.teamAScore);
//
//    final ParallelGroup teamBGroup = layout.createParallelGroup();
//    teamBGroup.addComponent(this.teamBLabel);
//    teamBGroup.addComponent(this.teamBScore);
//
//    if (timePanel == null) {
//      final SequentialGroup horizontalGroup = layout.createSequentialGroup();
//      horizontalGroup.addContainerGap(25, Short.MAX_VALUE);
//      horizontalGroup.addGroup(labelGroup);
//      horizontalGroup.addPreferredGap(ComponentPlacement.UNRELATED, 125, Short.MAX_VALUE);
//      horizontalGroup.addGroup(scoreGroup);
//      horizontalGroup.addContainerGap(25, Short.MAX_VALUE);
//      layout.setHorizontalGroup(horizontalGroup);
//
//      final SequentialGroup verticalGroup = layout.createSequentialGroup();
//      verticalGroup.addContainerGap(25, 50);
//      verticalGroup.addGroup(teamAGroup);
//      verticalGroup.addPreferredGap(ComponentPlacement.UNRELATED);
//      verticalGroup.addGroup(teamBGroup);
//      verticalGroup.addContainerGap(25, Short.MAX_VALUE);
//      layout.setVerticalGroup(verticalGroup);
//    }
//    else {
//      final ParallelGroup horizontalGroup = layout.createParallelGroup();
//      horizontalGroup.addGroup(
//          layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).addComponent(
//              this.teamALabel).addPreferredGap(ComponentPlacement.RELATED, 100, 100)
//              .addComponent(this.teamAScore).addContainerGap(25, Short.MAX_VALUE)).addGroup(
//          layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).addComponent(
//              this.teamBLabel).addPreferredGap(ComponentPlacement.RELATED, 100, 100)
//              .addComponent(this.teamBScore).addContainerGap(25, Short.MAX_VALUE)).addGroup(
//          layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).addComponent(
//              timePanel).addContainerGap(25, Short.MAX_VALUE));
//      layout.setHorizontalGroup(horizontalGroup);
//
//      final SequentialGroup verticalGroup = layout.createSequentialGroup();
//      verticalGroup.addContainerGap(25, Short.MAX_VALUE);
//      verticalGroup.addGroup(layout.createParallelGroup().addComponent(this.teamALabel)
//          .addComponent(this.teamAScore));
//      verticalGroup.addPreferredGap(ComponentPlacement.UNRELATED);
//      verticalGroup.addGroup(layout.createParallelGroup().addComponent(this.teamBLabel)
//          .addComponent(this.teamBScore));
//      verticalGroup.addPreferredGap(ComponentPlacement.UNRELATED);
//      verticalGroup.addComponent(timePanel);
//      verticalGroup.addContainerGap(25, Short.MAX_VALUE);
//      layout.setVerticalGroup(verticalGroup);
//    }
//
//    if (timePanel != null) {
//      this.frame.setTitle("eOSB Round Information");
//    }
//    this.frame.add(panel);
//    this.showFrame();
//  }
//
//  /**
//   * Displays the frame containing the {@link Scoreboard}
//   */
//  private void showFrame() {
//    this.frame.pack();
//    this.frame.setMinimumSize(this.frame.getPreferredSize());
//    this.frame.setLocationRelativeTo(null);
////    this.frame.setVisible(true);
//  }
//
//  /**
//   * Initializes the components.
//   */
//  private void initComponents() {
//
//    this.teamALabel.setFont(this.font);
//    this.teamAScore.setFont(this.font);
//    this.teamBLabel.setFont(this.font);
//    this.teamBScore.setFont(this.font);
//
//    this.teamALabel.setForeground(this.teamAColor);
//    this.teamAScore.setForeground(this.teamAColor);
//    this.teamBLabel.setForeground(this.teamBColor);
//    this.teamBScore.setForeground(this.teamBColor);
//  }
//
//  /**
//   * Updates the {@link Scoreboard} with new scores for the teams.
//   * 
//   * @param newTeamAScore team A's new score
//   * @param newTeamBScore team B's new score
//   */
//  public void updateScore(int newTeamAScore, int newTeamBScore) {
//    this.teamAScore.setText(Integer.toString(newTeamAScore));
//    this.teamBScore.setText(Integer.toString(newTeamBScore));
//  }
//
//  /**
//   * Updates the {@link Scoreboard} with new names for the teams.
//   * @param teamAName team A's new name
//   * @param teamBName team B's new name
//   */
//  public void updateTeamNames(String teamAName, String teamBName) {
//    this.teamALabel.setText(teamAName);
//    this.teamBLabel.setText(teamBName);
//  }
//
//  /**
//   * @return the frame containing the {@link Scoreboard}
//   */
//  public JFrame getFrame() {
//    return this.frame;
//  }
//
//  /** {@inheritDoc} */
////  @Override
//  public void onEvent(EventServiceEvent ese) {
//    if (ese instanceof TeamNameEvent) {
//      TeamNameEvent tne = (TeamNameEvent) ese;
//      this.teamALabel.setText(tne.getTeamAName());
//      this.teamBLabel.setText(tne.getTeamBName());
//    }
//    else if (ese instanceof TeamScoreNumberEvent) {
//      TeamScoreNumberEvent tsne = (TeamScoreNumberEvent) ese;
//      this.teamAScore.setText(Integer.toString(tsne.getTeamAScore()));
//      this.teamBScore.setText(Integer.toString(tsne.getTeamBScore()));
//    }
//  }
}
