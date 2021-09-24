/*
Name: Allan John Valiente, Student ID: 101285226
Name: Farah Sheherin, Student ID: 101297029
*/

package ca.gbc.comp2130.assignment2;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContactManager {
    private int maxNumContacts;
    private int numContacts;
    private Contact[] contactList;

    public ContactManager(int maxNumContacts) {
        this.maxNumContacts = maxNumContacts;
        numContacts = 0;
        contactList = new Contact[maxNumContacts];
    }

    public boolean addContact(String fName, String lName, String hPhone, String wPhone, Address homeAddress, String email, MyDate birthday, String notes) {
        if (numContacts >= maxNumContacts) {
            return false;
        }

        Contact contact = findContactByName(fName,lName);
        if (contact != null) {
            // Contact first and last names are unique.
            return false;
        }

        Contact c = new Contact(fName, lName, hPhone, wPhone, homeAddress, email, birthday, notes);
        int indexAvailable = 0;
        int cnt = 0;
        while(cnt<maxNumContacts){
            if(contactList[cnt]==null){
                indexAvailable = cnt;
                break;
            }
            cnt++;
        }
        contactList[indexAvailable] = c;
        numContacts++;
        return true;
    }

    public boolean updateContact(String fName, String lName, String hPhone, String wPhone, Address homeAddress, String email, MyDate birthday, String notes) {
        // Contact first and last names are unique.
        Contact contact = findContactByName(fName,lName);
        if (contact == null) {
            return false;
        }
        contact.setHomePhone(hPhone);
        contact.setWorkPhone(wPhone);
        contact.setHomeAddress(homeAddress);
        contact.setEmail(email);
        contact.setBirthday(birthday);
        contact.setNotes(notes);
        return true;
    }
    public boolean deleteContact(String firstName, String lastName) {
        int loc = findContactIndexByName(firstName,lastName);
        if (loc == -1) {
            return false;
        }
        contactList[loc] = null;
        numContacts--;
        return true;
    }

    public int getNumOfContacts() {
        return numContacts;
    }

    public Contact[] getAllContacts(){
        return contactList;
    }

    public Contact findContactByName(String firstName, String lastName) {
        int x = findContactIndexByName(firstName,lastName);
        if (x >= 0) {
            return contactList[x];
        }
        return null;
    }

    public ArrayList<Contact> findContactsByCity(String city) {
        city = city.toLowerCase();
        ArrayList<Contact> list = new ArrayList<Contact>();
        for (int x = 0; x < maxNumContacts; x++) {
            if (contactList[x]!= null &&
                contactList[x].getHomeAddress().city.toLowerCase().contains(city)) {
                list.add(contactList[x]);
            }
        }
        return list;
    }

    public ArrayList<Contact> findContactsByName(String name) {
        name = name.toLowerCase();
        ArrayList<Contact> list = new ArrayList<Contact>();
        for (int x = 0; x < maxNumContacts; x++) {
            if (contactList[x]!= null &&
                    (contactList[x].getFirstName().toLowerCase().contains(name)||contactList[x].getLastName().toLowerCase().contains(name))) {
                list.add(contactList[x]);
            }
        }
        return list;
    }

    public ArrayList<Contact> findContactsByNameAndCity(String name,String city) {
        name = name.toLowerCase();
        ArrayList<Contact> list = new ArrayList<Contact>();
        for (int x = 0; x < maxNumContacts; x++) {
            if (contactList[x]!= null &&
                ((contactList[x].getFirstName().toLowerCase().contains(name)||contactList[x].getLastName().toLowerCase().contains(name)) &&
                contactList[x].getHomeAddress().city.equals(city))
           ) {
                list.add(contactList[x]);
            }
        }
        return list;
    }

    public ArrayList<String> getCity() {
        ArrayList<String> list = new ArrayList<String>();
        for (int x = 0; x < maxNumContacts; x++) {
            if (contactList[x]!= null) {
                String city = contactList[x].getHomeAddress().city;
                if(!list.contains(city)){
                    list.add(city);
                }
            }
        }
        return list;
    }

    private int findContactIndexByName(String firstName, String lastName) {
        for (int x = 0; x < maxNumContacts; x++) {
            if (contactList[x]!= null &&
                (contactList[x].getFirstName().equals(firstName) && contactList[x].getLastName().equals(lastName))) {
                return x;
            }
        }
        return -1;
    }
}
