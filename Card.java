
/**
 * Makes a card with a rank, suit, and isfaceup.
 *
 * @author Anna Wang
 * @version November 3, 2017
 */
public class Card
{
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructs a Card facing down
     * @param r the rank.
     * @param s  the suit.
     */
    public Card(int r, String s)
    {
        rank = r;
        suit = s;
        isFaceUp = false;
    }

    /**
     * Returns the rank of the card.
     * @return  the rank
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Returns the suit of the card.
     * @return  the suit
     */
    public String getSuit()
    {
        return suit;
    }

    /**
     * Sees if the card is red or not (diamond or heart).
     * @return true if the card is red. else, false
     */
    public boolean isRed()
    {
        return (suit.equals("d") || suit.equals("h"));
    }

    /**
     * Sees if the card is face up or not
     * @return true if the card is face up
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }

    /**
     * Makes the card face up
     */
    public void turnUp()
    {
        isFaceUp = true;
    }

    /**
     * Makes the card face down.
     */
    public void turnDown()
    {
        isFaceUp = false;
    }

    /**
     * @return the file name
     */
    public String getFileName()
    {
        if (isFaceUp)
        {
            if (rank == 1)
            {
                return "cards/a" + suit + ".gif";
            }
            else if (rank == 10)
            {
                return "cards/t" + suit + ".gif";
            }
            else if (rank == 11)
            {
                return "cards/j" + suit + ".gif";
            }
            else if (rank == 12)
            {
                return "cards/q" + suit + ".gif";
            }
            else if (rank == 13)
            {
                return "cards/k" + suit + ".gif";
            }
            else
            {
                return "cards/" + rank + suit + ".gif";
            }
        }           
        return "cards/back.gif";
    }
}
