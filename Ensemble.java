package inf2120.tp3;
/**
 * @author Imad Bouarfa
 * Code permanent: BOUI24039303
 * Courriel: bouarfa.imad@courrier.uqam.ca
 * Cours: INF2120-10
 * @version 2023-04-30
 */


/**
 * Structure représentant un Ensemble de valeur entière.
 * Ces valeurs doivent être positives.  Le comportement de
 * la structure n'est pas définie pour des valeurs négatives.
 * Cette structure permet l'insertion et la suppression
 * d'élément.  Aussi, elle permet de consulter l'appartenance
 * d'un élément et le nombre d'élément contenu.
 *
 * Lorsque bien équilibré, cette structure effectue ces
 * tâche en O(log n).
 *
 * Lors de la construction, vous devez indiquer la taille
 * des noeuds (ratio).  Cette valeur est utilisé pour
 * déterminer le nombre d'enfant possible pour chaque noeud.
 *
 * Cette valeur influence les performances.  C'est la base du
 * logarithme pour l'algorithme de recherche.  Par contre, une
 * grande valeur va demander une plus grande utilisation de la
 * mémoire pour des ensembles de valeurs dispersées.
 */
public class Ensemble {
    protected NoeudAbstrait racine = null;

    public int nbValeur = 0;
    public int ratio = 0;

    /**
     * Constructeur.
     *
     * Ce constructeur construit un ensemble vide.
     * @param ratio Indique la taille maximum (nombre d'enfants)
     *              pour chaque noeud de l'arbre utilisé pour
     *              représenter l'ensemble.
     */
    public Ensemble( int ratio ) {
        this.ratio = ratio;
    }

    /**
     * Retourne le nombre d'élément qu'il y a dans l'ensemble.
     * @return la taille de l'ensemble.
     */
    public int getNbValeur() {
        return nbValeur;
    }

    /**
     * Ajoute un élément dans l'ensemble.
     *
     * Si cet élément n'était pas dans l'ensemble, il est
     * ajouté.  Sinon, l'ensemble n'est pas modifié.
     * @param element L'élément à ajouter.  L'élément ajouté
     *                ne doit pas être négatif.
     */
    public void inserer( int element ) {
        if (!appartient(element)){
            // Si l'élément n'existe pas, vérifie si la racine est null
            if (racine == null){
                racine = new Feuille(element);
            } else {
                if (racine instanceof Feuille){
                    // Si la racine est une feuille, crée un nouveau nœud interne pour la racine
                    int elementRacine = ((Feuille) racine).element;
                    racine = new Noeud(ratio);
                    ((Noeud) racine).enfants[elementRacine%ratio] = new Feuille(elementRacine/ratio);
                    // Insère l'élément dans le nouveau nœud interne
                    NoeudAbstrait enfant = ((Noeud) racine).enfants[element%ratio];
                    if (((Noeud) racine).enfants[element%ratio] == null){
                        ((Noeud) racine).enfants[element%ratio] = new Feuille(element/ratio);
                    } else if (enfant instanceof Feuille) {
                        int elementEnfant = ((Feuille) ((Noeud) racine).enfants[element%ratio]).element;
                        ((Noeud) racine).enfants[element%ratio] = new Noeud(ratio);
                        ((Noeud)((Noeud) racine).enfants[element%ratio]).inserer(elementEnfant);
                        ((Noeud)((Noeud) racine).enfants[element%ratio]).inserer(element/ratio);
                    }
                } else {
                    // Si la racine est un nœud interne, appelle "inserer" sur le nœud racine
                    ((Noeud) racine).inserer(element);
                }
            }
            nbValeur++;
        }

    }

    /**
     * Supprime un élément de l'ensemble.
     * Si cet élément était dans l'ensemble, alors il est
     * supprimé.  Sinon, l'ensemble n'est pas modifié.
     * @param element L'élément à supprimer.  L'élément
     *                supprimé ne doit pas être négatif.
     */
    public void supprimer( int element ) {
        // Vérifie si l'élément existe dans l'arbre
        if (appartient(element)){
            // Si l'élément existe, vérifie si la racine est une feuille
            if (racine instanceof Feuille){
                racine = null;
            } else {
                // Si la racine est un nœud interne, appelle "supprimerRec"
                racine = supprimerRec(element, racine);
            }
            nbValeur--;
        }
    }

    /**
     Supprime récursivement un élément donné à partir d'un nœud donné.
     @param element L'élément à supprimer.
     @param noeud Le nœud à partir duquel supprimer l'élément.
     @return Le nœud après la suppression de l'élément ou {@code null} si le nœud est vide après la suppression.
     */
    private NoeudAbstrait supprimerRec(int element, NoeudAbstrait noeud) {
        // Vérifie si le nœud est une feuille
        if (noeud instanceof Feuille) {
            noeud = null;
        } else {
            // Si le nœud est un nœud interne, appelle récursivement "supprimerRec" sur le sous-arbre approprié
            ((Noeud) noeud).enfants[element % ratio] = supprimerRec(element / ratio, ((Noeud) noeud).enfants[element % ratio]);
            // Vérifie si le sous-arbre a été vidé après la suppression de l'élément
            if (vide((Noeud) noeud)) {
                noeud = null;
            }
        }
        return noeud;
    }

    /**
     Vérifie si un nœud donné possède des enfants.
     @param noeud Le nœud à vérifier pour les enfants.
     @return {@code true} si le nœud n'a pas d'enfant, {@code false} sinon.
     */
    private boolean vide(Noeud noeud){
        for (NoeudAbstrait enfant: noeud.enfants){
            if (enfant != null){
                return false;
            }
        }
        return true;
    }


    /**
     * Vérifie l'appartenance d'un élément à l'ensemble.
     *
     * @param element L'élément cible.  Ne doit pas être négatif.
     * @return true si l'élément est dans l'ensemble, false sinon.
     */
    public boolean appartient( int element ) {
        // Vérifie si la racine est null (si l'arbre est vide)
        if (racine == null){
            return false;
        } else {
            // Si la racine est une feuille, compare l'élément avec l'élément
            if (racine instanceof Feuille){
                return ((Feuille) racine).element == element;
            } else {
                // Si la racine est un nœud, appelle la méthode "appartient" du nœud pour rechercher l'élément dans l'arbre
                return ((Noeud) racine).appartient(element);
            }
        }
    }

    /**
     * Construit une représentantion 'pretty print' de l'ensemble.
     *
     * Cette représentation est en format XML.
     * @param jolie
     */
    public void jolieToString( JolieAffichage jolie ) {
        JolieAffichage.Element eEnsemble = new JolieAffichage.Element( "ensemble" );
        jolie.afficherElementDebut( eEnsemble,
                new JolieAffichage.Attribut( "ratio", "" + ratio ),
                new JolieAffichage.Attribut( "nbValeur", "" + nbValeur )
                );

        if( null != racine ) {
            racine.jolieToString( jolie );
        }

        jolie.afficherElementFin( eEnsemble );
    }

    @Override
    public String toString() {
        JolieAffichage jolie = new JolieAffichage();
        jolieToString( jolie );

        return jolie.resultat();
    }
}
