import java.util.*;

public class Controller{
        
        /* This class allows for the players to interact with one another as well as with the pool*/
        
        protected static Player[] players; // Array of players to determine the players turn.
        
        protected static Pool pool; //Allows for controller to interact with the pool.
                
        protected static LinkedList<Card> deck; //Deck for the game.

        protected static LinkedList<Card> discardPile; //discard pile for the game.
        
        private static final int WINNING_SCORE = 150; // Score needed to win the game
        
        /*number of humans should always be one, and the number of *
         * bots is determined by the player when they start.*/
        public static void setupGame(int numOfBots){
                
                players = new Player[1 + numOfBots]; //array is set to the number of bots plus the human player
                                
                deck = new LinkedList<Card>();// initialize deck
                
                fillDeck(); // put all 52 cards in deck
                
                Collections.shuffle(deck); //The deck gets shuffled here.
                
                players[0] = new HumanPlayer(); //puts a human player in array at position 0, allowing for human to play first.
                
                /* for loop adding the number of bots to the remaining positions*/
                for(int i = 1; i < players.length; i++){
                        players[i] = new BotPlayer();
                }
                
                discardPile = new LinkedList<Card>(); //creating a discard pile that is a linked list of cards.
                discardPile.add(deck.pop()); //start discard pile

                pool = new Pool(); //new pool is created here
                
        }

        /* Method playCards allows for the player to try to play a hand.
         * The array of cards is passed to pool to see if the move is valid or not.
         * If it is valid the score is returned.*/
        public static int playCards(Card[] cards){
                int score = pool.score(cards);
                return score;
        }
        
        /* Method discard is called at the end of every players turn.
         * Adds the card that the player chooses to the top of the discard pile*/
        public static void discard(Card card){
                
                discardPile.push(card); //adds discard to the top of the pile.
                
        }
        
        /* This method allows for the controller to give a card to the player.
         * This method checks to see if the player wants to draw from deck or discard*/
        public static ArrayList<Card> giveCard(String source, int numberOfCards){
                
                ArrayList<Card> n = new ArrayList<Card>();
        
                /*if the player states that they want to draw from the discard*/
                if(source.equals("discard"))
                {
                    	/*loop to remove cards chosen from discard to array and passes to player hand.*/
                    	for(int i = 0; i < numberOfCards; i++)
                                n.add(discardPile.pop());
                }
                
                /*this loop is only for the start of the game. Deals starting hand to players.*/
                else if(source.equals("deck start")){
                        int i = 0;
                        while(i < 7){
                                n.add(deck.pop());
                                i++;
                        }
                }
                
                /*removes one card from deck and adds to players hand.*/
                else if(source.equals("deck")){
                        n.add(0,deck.pop());
                }
                return n;
        }

        @Override
        public String toString() {
                return Arrays.toString(players);
        }
        

        public static void main(String[] args){
                
                int NumberOfBots = 0; //how many bots
        
                System.out.println("---------------- Welcome To Rummy! -----------------");
                System.out.println("The objective of this game is to reach "+WINNING_SCORE+" points.\nThe first player to reach this objective wins.\n----------------------------------------------------\n");
                
                Scanner sc = new Scanner(System.in); //creating scanner for input from user
                
                boolean validResponse = false;
                
                 while(!validResponse) // another loop asking for input until it receives a valid response
                 {
			         System.out.println("How many Bots would you like to play against? (1 - 3)");
			            
			         try
			         {
			         NumberOfBots = sc.nextInt();
			         }
			         catch(InputMismatchException e) // this exception will be thrown if anything but an integer is input
			         {        
			                          System.out.println("Invalid statement, please re-enter a number.");
			                          sc.nextLine(); // clears scanner for new input (important)
			                          continue; // start next loop iteration
			         }
			         if(NumberOfBots <= 3 && NumberOfBots > 0){
			                  validResponse = true;
			         }
			         else{
			                  System.out.println("Invalid number, please re-enter a number.");
			         }
		         }
                 
                setupGame(NumberOfBots); // Creating new Controller that takes in number of bots and one human
                
                Player human = new HumanPlayer(); //human player here.
        
                players[0] = human; //human player will always be at position 0
        
                
                boolean KeepPlayingGame = true;
                boolean KeepPlayingRound = true;
                boolean FirstRound = true; // the game starts at the first round
                
                while(KeepPlayingGame)
                {
                	
                	 
                     System.out.println("A new round is starting.");
                     
                     /* For the first round, all the setup is taken care of in the constructor.
                      * For the remaining rounds, need to reset deck and discard, and re-deal hands to players.
                      */
                     if(!FirstRound) 
                     {
	                     deck.clear(); //empty deck
	                     
	                     fillDeck(); // fill deck 
	                     
	                     Collections.shuffle(deck); //shuffle deck
	                     
	                     players[0].getHand().clear(); // empty human player's hand
	                     players[0].setHand(Controller.giveCard("deck start", 7)); // deal human player new hand
	                     
	                     for(int i = 1; i < players.length; i++) // create new bots for next round
	                     {
	                    	 int temp = players[i].getScore(); // need to keep came score from last round 
	                         players[i] = new BotPlayer(); // create and give cards to new bot
	                         players[i].setScore(temp); // give bot right score
	                     }
	                     
	                     discardPile.clear(); //clear discard pile
	                     discardPile.add(deck.pop()); //start discard pile
	                     
	                     pool = new Pool(); // create new, empty pool 
                     }
                     
                     int turn = 0;
                     Player PlayerTurn = players[turn];
                     
                     KeepPlayingRound = true;

	                //so long as this is true, the round will continue.
	                while(KeepPlayingRound){
	                        
	                        /* This if statement checks to see if the deck is now empty.
	                         * if it is empty, then the deck obtains cards from the discard pile.
	                         * The deck is then shuffled again, and discard obtains a card from deck.*/
	                        if(deck.isEmpty())
	                        {
	                        		System.out.println("\nThe deck is empty. The discard pile will be shuffled and placed into the deck.\n");
	                                while(!discardPile.isEmpty()) {
	                                        deck.push(discardPile.pop());
	                                }
	                                
	                                Collections.shuffle(deck);
	                                discardPile.add(deck.pop());
	                        }
	                        
	                        /* This is if the player is not a human player.*/
	                        if(turn != 0){
	                        		
                                	System.out.println("\nIt is now Player " + turn + "'s turn.");

	                                PlayerTurn.draw(); //bot draws.
	                                PlayerTurn.play(); //bot can play
	                                PlayerTurn.DiscardFromHand(); //bot discards from hand.
	                                
	                                for(int i = 0; i < PlayerTurn.getHand().size(); i++){
	                                        System.out.print("[\u25AE] "); //hides bot hand from human player
	                                }
	                                System.out.println("Player " + turn +" Score: " + players[turn].getScore() + "\n\n~~~~~~~~~~~~~~~~~~~~~");
	                        
	                        }
	                        
	                        /* This is if the player is a human player.*/
	                        else{
	                        System.out.println("\nScore: " + PlayerTurn.getScore() + "\nHand: " + PlayerTurn.getHand() + "\nDiscard Pile: " + discardPile + "\nPool\n---------------------\n" + pool + "---------------------\n");                        
	                        
	                        PlayerTurn.draw(); //human draws.
	                        System.out.println("\nHand: " + PlayerTurn.getHand() + "\nDiscard Pile: " + discardPile + "\nPool\n---------------------\n" + pool + "---------------------\n");
	                
	                        PlayerTurn.play(); //human can play.
	                
	                        PlayerTurn.DiscardFromHand(); //human discards.
	                        
	                        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~");
	                        }
	                        
	                        //if player has no more cards, then round is over
	                        KeepPlayingRound = PlayerTurn.getHand().size() != 0;
	                        
	                        //keeps track of the turns.
	                        turn = (turn + 1) % players.length;
	                        
	                        //keeps track of the turns.
	                        PlayerTurn = players[turn];
	                }
	                
	                FirstRound = false; // if a round has ended, then it is not the first round
	                
	                if(turn == 1) // print "round is over"
	                	System.out.println("You have played all the cards in your hand. This round is over."); // you played all your cards
	                else
	                	System.out.println("Player "+ ((turn+players.length-1) % players.length) +" has played all the cards in their hand. This round is over."); // last bot to play played all their cards
	                
	                for(int i=0;i<players.length;i++) // deduct values of remaining cards in players' hands from their scores 
	                {
	                	for(int j=0;j<players[i].getHand().size();j++) // loop through player i's hand
	                	{
	                		players[i].setScore(players[i].getScore()-players[i].getHand().get(j).getValue()); // deduct card value from score for each card in hand
	                	}
	                }
	                
	                System.out.println("After deducting points for the remaining cards in each player's hand: \nYou have " + players[0].getScore() + " points.");
	                for(int i=1;i<players.length;i++)
	                {
	                	System.out.println("Player " + i + " has " + players[i].getScore() + " points.");
	                }
	                
	                
	                
	                /*loop to check for if a players score is >= WINNING_SCORE. if it is, then the game is over and that player wins.*/
	                int highestScore = players[0].getScore(); // highest score of all players
	                int index = 0; // index in players[] of player with highest score
	                for(int i = 1; i < players.length; i++){ // this loop determines which player has the highest score
	                        if(players[i].getScore() >highestScore)
	                        {
	                                highestScore = players[i].getScore();
	                                index = i;
	                        }
	                }
	                if(highestScore>=WINNING_SCORE) // the player with the highest score wins if they have at least WINNING_SCORE points
	                {
	                	KeepPlayingGame = false;
	                	
	                	if(index==0)
	                		 System.out.println("You win!");
	                	else
	                		 System.out.println("Player " + index + " wins!");
	                }
	                
                }
        }
        
        
        /*This method puts the usual 52 cards into the deck.
         */
        private static void fillDeck()
        {
        	Card h2 = new Card("2",'H');
			Card h3 = new Card("3",'H');
			Card h4 = new Card("4",'H');
			Card h5 = new Card("5",'H');
			Card h6 = new Card("6",'H');
			Card h7 = new Card("7",'H');
			Card h8 = new Card("8",'H');
			Card h9 = new Card("9",'H');
			Card h10 = new Card("10",'H');
			Card hJ = new Card("J",'H');
			Card hQ = new Card("Q",'H');
			Card hK = new Card("K",'H');
			Card hA = new Card("A",'H');
			Card c2 = new Card("2",'C');
			Card c3 = new Card("3",'C');
			Card c4 = new Card("4",'C');
			Card c5 = new Card("5",'C');
			Card c6 = new Card("6",'C');
			Card c7 = new Card("7",'C');
			Card c8 = new Card("8",'C');
			Card c9 = new Card("9",'C');
			Card c10 = new Card("10",'C');
			Card cJ = new Card("J",'C');
			Card cQ = new Card("Q",'C');
			Card cK = new Card("K",'C');
			Card cA = new Card("A",'C');
			Card s2 = new Card("2",'S');
			Card s3 = new Card("3",'S');
			Card s4 = new Card("4",'S');
			Card s5 = new Card("5",'S');
			Card s6 = new Card("6",'S');
			Card s7 = new Card("7",'S');
			Card s8 = new Card("8",'S');
			Card s9 = new Card("9",'S');
			Card s10 = new Card("10",'S');
			Card sJ = new Card("J",'S');
			Card sQ = new Card("Q",'S');
			Card sK = new Card("K",'S');
			Card sA = new Card("A",'S');
			Card d2 = new Card("2",'D');
			Card d3 = new Card("3",'D');
			Card d4 = new Card("4",'D');
			Card d5 = new Card("5",'D');
			Card d6 = new Card("6",'D');
			Card d7 = new Card("7",'D');
			Card d8 = new Card("8",'D');
			Card d9 = new Card("9",'D');
			Card d10 = new Card("10",'D');
			Card dJ = new Card("J",'D');
			Card dQ = new Card("Q",'D');
			Card dK = new Card("K",'D');
			Card dA = new Card("A",'D');
			deck.push(h2);
			deck.push(h3);
			deck.push(h4);
			deck.push(h5);
			deck.push(h6);
			deck.push(h7);
			deck.push(h8);
			deck.push(h9);
			deck.push(h10);
			deck.push(hJ);
			deck.push(hQ);
			deck.push(hK);
			deck.push(hA);
			deck.push(c2);
			deck.push(c3);
			deck.push(c4);
			deck.push(c5);
			deck.push(c6);
			deck.push(c7);
			deck.push(c8);
			deck.push(c9);
			deck.push(c10);
			deck.push(cJ);
			deck.push(cQ);
			deck.push(cK);
			deck.push(cA);
			deck.push(s2);
			deck.push(s3);
			deck.push(s4);
			deck.push(s5);
			deck.push(s6);
			deck.push(s7);
			deck.push(s8);
			deck.push(s9);
			deck.push(s10);
			deck.push(sJ);
			deck.push(sQ);
			deck.push(sK);
			deck.push(sA);
			deck.push(d2);
			deck.push(d3);
			deck.push(d4);
			deck.push(d5);
			deck.push(d6);
			deck.push(d7);
			deck.push(d8);
			deck.push(d9);
			deck.push(d10);
			deck.push(dJ);
			deck.push(dQ);
			deck.push(dK);
			deck.push(dA);
        }
}

//* 11/18/2013 4:04(PM): working on testing out starting situations. Might make new method for choices player makes.
//                                                  feel free to test the game if you want. As you will notice after a deck or discard draw,
//                                                 the player's hand gets replaced with what they drew. Need to fix that.
//
//* 11/19/2013 6:03(PM): Completed prototype for Human player and controller interaction. Minor fixes need to be made
//                                                 in human player. Currently allows the play of the same card multiple times. Found out that
//                                                 if you really wanted to, you could play by yourself and it works just fine.
//
//
//* 12/2/2013 1:51 (PM): Added complete set of comments for final submission. 
//
//* 12/8/2013: Game play is now in rounds. If the player with the most points has at least WINNING_SCORE points, they win, otherwise a new round
//			   starts. Rounds end when somebody plays all the cards in there hand. At the end of every round (before checking for a winner), any
//             player with cards left in their hand has the value of those cards deducted from their score. 
//
//             Fixed issue with refilling deck after all cards have been drawn by implementing the deck using library code instead of the previously
//             written deck class. This update removes the need for the separate Deck class.