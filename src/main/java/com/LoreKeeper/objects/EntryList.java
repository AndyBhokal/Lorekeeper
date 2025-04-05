package com.LoreKeeper.objects;
import java.util.ArrayList;
import java.util.Iterator;

import com.LoreKeeper.exceptions.EntryListException;

public class EntryList implements Iterable<Entry> {
    private ArrayList<Entry> entryList;
    private Entry deletedEntry;
    private Entry editedEntry;
    private Action prevAction = Action.NOTHING;


    enum Action {
        REMOVE,
        ADD,
        EDIT,
        UNDO,
        NOTHING
    }

    public EntryList(){
        entryList = new ArrayList<Entry>();
    }

    /**
     * Adds an entry at the position indicated by its token.
     * If the token already exists in the entryList, throws an error.
     * @param newEntry
     * @throws EntryListException
     */
    public void add(Entry newEntry) throws EntryListException{
        if(entryList.contains(newEntry)){
            throw new EntryListException("This entry already exists.");
        }
        entryList.add(/*newEntry.getToken(),*/ newEntry);
        // System.out.println(newEntry.getToken()+ " "+newEntry.toString());
        prevAction = Action.ADD;
    }



    public void edit(String message) throws EntryListException{
        edit(entryList.size()-1, message);
    }


    public void edit(int token, String message) throws EntryListException{
        if(entryList.isEmpty()){
            throw new EntryListException("Can't edit nothing!");
        }
        editedEntry = entryList.get(token);
        entryList.get(token).edit(message);
        prevAction = Action.EDIT;
 
    }

    public Entry getEntry(int token){
        return entryList.get(token);
    }

    public boolean isEmpty(){
        return entryList.isEmpty();
    }
    
    public void undo() throws EntryListException{
        switch(prevAction){
            case REMOVE:
                entryList.add(deletedEntry);
                break;

            case ADD:
                remove();
                break;

            case UNDO:
                throw new EntryListException("Cannot undo an undo!");  

            case EDIT:
                edit(editedEntry.getToken(), editedEntry.getMessage());
                break;

            default:
                throw new EntryListException("Nothing to undo!");
        }
        prevAction = Action.UNDO;
    }

    class EntryListIterator implements Iterator<Entry>{
        private int index = 0; 
        @Override
        public boolean hasNext() {
            return index < entryList.size();
        }

        @Override
        public Entry next() {
            if(!hasNext()){
                return null;
            }
            else {
                int tempIndex = index;
                index++;
                return entryList.get(tempIndex);
            }
        }
    }

    @Override
    public Iterator<Entry> iterator() {
        return new EntryListIterator();
    }

    /**
     * Calls {@code remove(int token)} with the last Entry token
     * @throws EntryListException
     */
    public void remove() throws EntryListException{
        remove(entryList.size()-1);
    }
    
    /**
     * Removes the Entry with the specified token from the EntryList. 
     * Throws an error if the EntryList is empty or the Entry does not exist. 
     * 
     * @param token
     * @throws EntryListException
     */
    public void remove(int token) throws EntryListException{
        if(entryList.isEmpty()){
            throw new EntryListException("Nothing to remove!");
        }
        try{
            deletedEntry = entryList.get(token);
            entryList.remove(token);
            prevAction = Action.REMOVE;
        } catch(IndexOutOfBoundsException e){
            throw new EntryListException("The entry does not exist!");
        }
    }

}