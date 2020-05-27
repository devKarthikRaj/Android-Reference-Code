package com.raj.allthingsrecyclerview;

/*
    An interface in the Java programming language is an abstract type that is used to specify a behavior that classes must implement.
    They are similar to protocols. Interfaces are declared using the interface keyword, and may only contain method signature and constant
    declarations.

    So this interface defines the methods that HAVE to be implemented when implementing this interface
*/

public interface RecyclerViewClickInterface {
    void onItemClick(int position);
    void onLongItemClick(int position);
}
