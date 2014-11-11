// ____________________________ \\
// ** _ ** BSPNode.java ** _ ** \\
// ** ^ **   Joe Krall  ** ^ ** \\
// ```````````````````````````` \\

package aimazed2d;

public class BSPNode {
    
    int x, y, width, height;
    BSPNode child1, child2;
    BSPNode parent;
    
    public BSPNode(int x1, int y1, int w, int h)
    {
        x = x1;
        y = y1;
        width = w;
        height = h;        
    }
    
    public BSPNode getParent()
    {
        return parent;
    }
    
    public BSPNode getLeftChild()
    {
        return child1;
    }
    
    public BSPNode getRightChild()
    {
        return child2;
    }
    
    
}
