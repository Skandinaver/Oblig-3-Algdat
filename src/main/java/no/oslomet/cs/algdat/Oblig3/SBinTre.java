package no.oslomet.cs.algdat.Oblig3;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.Queue;
import java.util.LinkedList;

public class SBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {                                   //Mye av denne koden er kopiert fra kompendiet kildekode 5.2 3 a)
        Objects.requireNonNull(verdi, "Ulovelig med nullverdi!");       //Sjekker at verdi ikke er null

        Node<T> p = rot, q = null;                                      //Lager to peker noder og setter p til rot
        int cmp = 0;                             
    
        while (p != null)                                               //While løkke ender når p er på tom plass
        {
          q = p;                                                        //q settes til p slik at den senere kan brukes som forelder
          cmp = comp.compare(verdi,p.verdi);                            //Sammenligner verdi med verdi i noden slik at vi vet hvor vi skal navigere/legge inn ny node
          p = cmp < 0 ? p.venstre : p.høyre;     
        }
        p = new Node<T>(verdi, null, null, q);                          //Setter p til ny node uten barn med foreldre q.               
    
        if (q == null) rot = p;                                         //Om q er null så settes rot til den nye noden
        else if (cmp < 0) q.venstre = p;                                //Om comparator returnerer mindre enn null legges verdi inn som venstere barn
        else q.høyre = p;                                               //Ellers legges det inn som høyre barn
    
        antall++;                                                       //Øker antall
        return true;                                                    //returnerer true når verdi er lagt inn
        
    }

    public boolean fjern(T verdi) {                                     //Hentet fra kompendiet etter instruks fra oblig, foreldre blir også satt riktig nå
        if (verdi == null) return false;                                // treet har ingen nullverdier

        Node<T> p = rot, q = null;                                      // q skal være forelder til p
    
        while (p != null)                                               // leter etter verdi
        {
          int cmp = comp.compare(verdi,p.verdi);                        // sammenligner
          if (cmp < 0) { q = p; p = p.venstre; }                        // går til venstre
          else if (cmp > 0) { q = p; p = p.høyre; }                     // går til høyre
          else break;                                                   // den søkte verdien ligger i p
        }
        if (p == null) return false;                                    // finner ikke verdi
    
        if (p.venstre == null || p.høyre == null)                       // Tilfelle 1) og 2)
        {
          Node<T> b = p.venstre != null ? p.venstre : p.høyre;          // b for barn
            if (p == rot) {
              rot = b;
              if(b != null) b.forelder = null;
            }
            else if(p == q.venstre){
               q.venstre = b;
               if(b != null) b.forelder = q;
            }
            else{
               q.høyre = b;
               if(b != null) b.forelder = q;
            }

        }
        else                                                            // Tilfelle 3)
        {
          Node<T> s = p, r = p.høyre;                                   // finner neste i inorden
          while (r.venstre != null)
          {
            s = r;                                                      // s er forelder til r
            r = r.venstre;
          }
    
          p.verdi = r.verdi;                                            // kopierer verdien i r til p
    
            if (s != p){
               s.venstre = r.høyre;
               if(s.venstre != null) s.venstre.forelder = s;
            }
            else{
               s.høyre = r.høyre;
               
            }
        }
    
        antall--;                                                       // det er nå én node mindre i treet
        return true;    
    }

    public int fjernAlle(T verdi) {                                     //Fjerner alle instanser av ønsket verdi
        boolean loop = true;                                            //Støtte-variabel
        int numberRemoved = 0;                                          //antall instanser fjernet
        while(loop){                                                    //Så lenmge loop er sann
            loop = fjern(verdi);                                        //loop er sann så lenge verdi kan fjernes fra treet
            if(loop){
                numberRemoved++;                                        //inkrementerer antall fjernet om loop fortsatt er sann
            }
        }
        return numberRemoved;                                           //returnerer antall fjernet
    }

    public int antall(T verdi) {                
        if(verdi == null){                                                  //null verdi ikke tilatt i treet returnerer 0
            return 0;
        }
        return _antall(rot, verdi, 0);                                      //Kjører den rekursive hjelpemetoden på hele treet
    }

    private int _antall(Node<T> pointer, T verdi, int antall){              //Rekursiv hjelpemetode for antall-metoden
        if(pointer == null){                                                //null verdi ikke tillat i treet så returnerer umidelbart
            return antall;
        }
        if(pointer.verdi == verdi){                                         //Inkrementerer antall om verdi i node er ønsket verdi
            antall++;
        }
        antall = _antall(pointer.venstre, verdi, antall);                   //Kaller hjelpemetode på venstre barn
        antall = _antall(pointer.høyre, verdi, antall);                     //Kaller hjelpemetode på høyre barn

        return antall;
    }

    public void nullstill() {                                               //Metode som tømmer treet på tungvint vis
        if(rot == null) return;                                             //Om rot er null er treet tomt
        Node<T> p = førstePostorden(rot);                                   //Finner første node i postorden
        Node<T> prev = null;                                                //Setter en peker for å holde styr på forrige node
        while(p != null){                                                   //Så lenge p ikker er null
            prev = p;                                                       //setter prev peker til p 
            p = nestePostorden(p);                                          //setter p til neste node i postorden
            if(prev != null){                                               //setter alle verdiene i prev til null
            prev.forelder = null;
            prev.høyre = null;
            prev.venstre = null;
            prev.verdi = null;
            }
        }
        antall = 0;                                                         //nuller antall
        rot = null;                                                         //nuller rot
    }

    private static <T> Node<T> førstePostorden(Node<T> p) { //Metode for å finne første node i treets postorden

        while(true){                                        //While-løkke
            if(p.venstre != null){                          //om venstre barn ikke er tomt traverserer det ut mot venstre
                p = p.venstre;
            }
            else if(p.høyre != null){                       //Sjekker så om et høyre barn eksisterer
            p = p.høyre;
            }
            else{                                           //Om hverken høyre eller venstre barn blir funnet er vi på riktig sted
                break;
            }
        }
        return p;                                           //Returnerer noden
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {  //Metode for å finne neste node i postorden
        if(p.forelder == null){                             //Om forelder er null er det siste node i postorden
            return null;
        }
        else if(p.forelder.høyre == p){                     //om forrige i postorden var høyrebarn så returnerer vi foreldren
            return p.forelder;
        }       
        else{                                               //Ellers om det ikke er noe høyrebarn var vi et venstre barn og vi må returnere oreldre
            if(p.forelder.høyre == null){
                return p.forelder;
            }
            return førstePostorden(p.forelder.høyre);       //Om det er et høyrebarn og vi var venstrebarn så kjører vi første-postorden funksjon på høyrebarnet for 
        }                                                   //å finne neste postorden
    }

    public void postorden(Oppgave<? super T> oppgave) {
        
        Node<T> p = førstePostorden(rot);                   //Setter en peker til første i postorden
        
        while(p != null){                                   //Så lenge p ikke er null er det fremdeles en neste i rekken
            oppgave.utførOppgave(p.verdi);                  //sender in verdien i noden til oppgaven
            p = nestePostorden(p);                          //setter p til neste node i postorden
        }

    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {                    //Kjører hjelpemetoden på treet fra rot
        postordenRecursiveHelp(rot, oppgave);
        
    }

    private void postordenRecursiveHelp(Node<T> node, Oppgave<? super T> oppgave){  //Hjelpemetode for postordenrecursive
        if(node == null){                                                   
            return;
        }
        postordenRecursiveHelp(node.venstre, oppgave);                              //Metoden traverserer treet i postorden og gjør oppgaven på hver node
        postordenRecursiveHelp(node.høyre, oppgave);

        oppgave.utførOppgave(node.verdi);

    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public ArrayList<T> serialize() {
        ArrayList<T> serializedTree = new ArrayList<T>();                       //Lager ny array list
        Queue<Node<T>> queue=new LinkedList<Node<T>>();                         //Lager en kø
        queue.add(rot);                                                         //Legger rot inn i køen
        while(!queue.isEmpty())                                                 //While-løkke kjører så lenge det finnes et element i køen
        {
            Node<T> tempNode=queue.poll();                                      //henter ett element fra køen og legger det i en midlertidig node
            serializedTree.add(tempNode.verdi);                                 //Legger verdien inn i array
            if(tempNode.venstre!=null){
                queue.add(tempNode.venstre);                                    //Om det er et venstre barn legger vi det inn i køen
            }
            if(tempNode.høyre!=null){                                           //Om det er et høyrebarn legger vi det inn i køen
                queue.add(tempNode.høyre);
            }
        }
        return serializedTree;                                                  //Returnerer arrayet
    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {

        SBinTre<K> tre = new SBinTre<K>(c);                                     //Lager nytt tre                
        for (K k : data) {                                                      //for hver hver instans av k fra arrayet data
            tre.leggInn(k);                                                     //legger vi k inn i treet vårt
        }
        return tre;                                                             //returnerer tre
        

    }


} // ObligSBinTre
