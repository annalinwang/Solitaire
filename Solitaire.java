import java.util.*;
/**
 * Game of Solitaire
 * @author Anna Wang
 * @version November 12, 2017
 */
public class Solitaire 
{
    /**
     * Makes a new solitaire object
     * @param args  argument
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    /**
     * Instantiates a new Solitaire
     */
    public Solitaire()
    {
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        foundations = new Stack[4];
        for (int i = 0; i < 4; i++)
        {
            foundations[i] = new Stack < Card>();
        }
        piles = new Stack[7];
        for (int i = 0; i < 7; i++)
        {
            piles[i] = new Stack<Card>();
        }
        createStock();
        deal();
        display = new SolitaireDisplay(this);

    }

    /**
     * Returns Card on the top of the stock, null if the stock is empty
     * @return  the stock card
     */
    public Card getStockCard()
    {
        if (stock.isEmpty())
        {
            return null;
        }
        else
        {
            return stock.peek();
        }
    }

    /**
     * Returns card on the top of the waste, null if the waste is empty
     * @return  the waste card
     */
    public Card getWasteCard()
    {
        if (waste.isEmpty())
        {
            return null;
        }
        else
        {
            return waste.peek();
        }
    }

    /**
     * @param index is the index of the foundation array 
     *        in which the card is peeked from the stack
     * @return the top card of the queue 
     *         at the given index of the foundation
     * @precondition 0 <= index < 4
     * @postcondition: returns the card on top of the given foundation, 
     *                 or null if the foundation is empty
     */
    public Card getFoundationCard(int index)
    {
        if (foundations[index].isEmpty())
        {
            return null;
        }
        else
        {
            return foundations[index].peek();
        }
    }

    /**
     * @precondition: 0 <= index < 7
     * @postcondition: returns a reference to the given pile
     * @param index is the index of piles in which the stack is returned
     * @return the stack at the given index of piles
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * Creates an ArrayList containing each of the 52 cards in a standard deck. 
     * Then, repeatedly remove a random card from this ArrayList and add it to 
     * the stock, until there are no cards left to remove.
     */
    public void createStock()
    {
        ArrayList<Card> myList = new ArrayList<Card>();
        for (int i = 1; i <= 13; i++)
        {
            myList.add(new Card(i , "c"));
            myList.add(new Card(i , "d"));
            myList.add(new Card(i , "h"));
            myList.add(new Card(i , "s"));
        }
        while (myList.size() != 0)
        {
            double randIndex = Math.random() * myList.size();
            stock.push(myList.remove((int)randIndex));
        }
    }

    /**
     * Deals cards from the stock to the 7 piles
     * Top card is faced up
     */
    public void deal()
    {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j <= i; j++)
            {
                piles[i].push(stock.pop());               
            }
            piles[i].peek().turnUp();
        }
    }

    /**
     * Moves the top three cards from the stock onto the top of the waste
     * If there are less than 3 cards, transfer whatever is left to the waste
     * Turn up each of the cards moved onto the waste
     */
    public void dealThreeCards()
    {
        for (int i = 0; i < 3; i++)
        {
            if (!stock.isEmpty())
            {
                waste.push(stock.pop());
                waste.peek().turnUp();
            }
        }
    }

    /**
     * Moves the top card from the waste to the top of the stock
     * until the waste is empty
     * Turn down each card moved to the stock
     */
    public void resetStock()
    {
        while (!waste.isEmpty())
        {
            stock.push(waste.pop());
            if (stock.peek().isFaceUp())
            {
                stock.peek().turnDown();
            }
        }
    }

    /**
     * Called when the stock is clicked by user
     * If the stock has any cards left, transfer three cards to the waste
     * If waste or pile is selected, do nothing
     * Otherwise, reset the stock
     */
    public void stockClicked()
    {
        if (stock.isEmpty())
        {
            resetStock();
        }
        else
        {
            dealThreeCards();
        }
        System.out.println("stock clicked");
    }

    /**
     * Called when the waste is clicked
     * Selects the waste if it is not empty
     * Unselects the waste if it is already selected
     */
    public void wasteClicked()
    {
        if (!waste.isEmpty())
        {
            if (display.isWasteSelected())
            {
                display.unselect();
            }
            else
            {
                display.selectWaste();
            }
        }
        System.out.println("waste clicked");
    }

    /**
     * @precondition 0 <= index < 7
     * @param index
     * Called when given pile is clicked
     * If waste is selected, move the top of the card from the waste onto the 
     * top of the given pile and unselect the waste
     */
    public void pileClicked(int index)
    {
        if (display.isWasteSelected()) 
        {
            if (!waste.isEmpty())//idk
            {
                if (canAddToPile(waste.peek(), index)) 
                {
                    piles[index].push(waste.pop());
                }
                display.unselect();

            }
        }
        else if (!display.isWasteSelected() && !display.isPileSelected()) 
        {
            if (!piles[index].isEmpty())//idk
            {
                if (piles[index].peek().isFaceUp())
                {
                    display.selectPile(index);
                }
                else
                {
                    piles[index].peek().turnUp();
                }
            }
        }
        else if (display.isPileSelected())
        {
            if (display.selectedPile() == index)
            {
                for (int i = 0; i < 4; i++)
                {
                    if (!piles[index].isEmpty())
                    {
                        if (canAddToFoundation(piles[index].peek(), i))
                        {
                            foundations[i].push(piles[index].pop());
                            i = 4;
                        }
                    }
                }
                display.unselect();
            }
            else
            {
                Stack <Card> temp = new Stack<Card>();
                temp = removeFaceUpCards(display.selectedPile());
                if (!temp.isEmpty())
                {
                    if (canAddToPile(temp.peek(), index))
                    {
                        addToPile(temp, index);
                        display.unselect();
                        display.selectPile(index);
                    }
                    else
                    {
                        addToPile(temp, display.selectedPile());
                    }
                }
            }
        }
        System.out.println("pile #" + index + " clicked");
    }

    /**@precondition: 0 <= index < 7
     * @postcondition: Returns true if the given card can be
     * legally moved to the top of the given
     * pile
     **/
    private boolean canAddToPile(Card card, int index)
    {
        if (piles[index].isEmpty() && card.getRank () != 13)
        {
            return false;
        }
        if (piles[index].isEmpty() && card.getRank() == 13)
        {
            return true;
        }
        else if (card.isRed() != piles[index].peek().isRed())
        {
            if (card.getRank() == piles[index].peek().getRank() - 1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @precondition: 0 <= index < 7
     * @postcondition: removes all face-up cards on the top of the given pile;
     *                 returns a stack containing these cards
     * @param index the index to remove the face up cards
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> myStack = new Stack<Card>();
        while (!piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {
            myStack.push(piles[index].pop());
        }        
        return myStack;
    }

    /**
     * @precondition: 0 <= index < 7
     * @postcondition: Removes elements from cards, and adds them 
     * to the given pile.
     * @param   cards    the cards
     * @param   index   the index to add
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while (!cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }

    /**
     * @precondition: 0 <= index < 4
     * @postcondition: returns true if the given card can be legally moved
     *                 to the top of the given foundation
     * @param   card    the card
     * @param   index   the index to add
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        if (card.isFaceUp())
        {
            if (foundations[index].isEmpty())
            {
                if (card.getRank() == 1)
                {
                    return true;
                }
            }
            else if (card.getSuit().equals(foundations[index].peek().getSuit())
            && card.getRank() == foundations[index].peek().getRank() 
            + 1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @precondition:  0 <= index < 4
     * @param index
     * Called when given foundation is clicked
     * Tests if the selected card (either the waste card or 
     * the top card of the selected pile)
     * can be legally added to the given foundation
     * If yes, move the card and unselect the source
     */
    public void foundationClicked(int index)
    {
        if (display.isWasteSelected() && 
        canAddToFoundation(waste.peek(), index))
        {
            foundations[index].push(waste.pop());
        }
        else if (display.isPileSelected() &&
        canAddToFoundation(piles[display.selectedPile()].peek(), index))
        {
            foundations[index].push(piles[display.selectedPile()].pop());
        }
        System.out.println("foundation #" + index + " clicked");
    }
}