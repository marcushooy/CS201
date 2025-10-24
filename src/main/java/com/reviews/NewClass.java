package com.reviews;

public class NewClass {

    public void add(Gameentry e){
        int newScore = e.getScore();
        
    // check if the score is really the high score
    if (numEntires < board.length || newScore > board[numEntires-1].getScore()){
        if (numEntires < board.length){
            numEntires++;
        }

        int j = board.length - 1;
        while(j > 0 && newScore > board[j - 1].getScore()){
            board[j] = board[j -1];
            j--;
        }

        board[j] = e;
}
    }
}

public E removeLast(){
    Node walk = head;
    while (walk.getNext() != tail){
        walk = walk.getNext();
    }
    walk.setNext(null);
    tail = walk;
}


