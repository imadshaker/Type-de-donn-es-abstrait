package inf2120.tp3;

/**
 * Cette classe contient l'information pour un Noeud.
 * Il permet de lier plusieurs enfants dans la structure.
 */
class Noeud extends NoeudAbstrait {
    protected NoeudAbstrait[] enfants;

    public Noeud(int ratio) {
        enfants = new NoeudAbstrait[ratio];
    }

    @Override
    public void jolieToString(JolieAffichage jolie) {
        JolieAffichage.Element eNoeud = new JolieAffichage.Element("noeud");
        JolieAffichage.Element eCase = new JolieAffichage.Element("case");
        jolie.afficherElementDebut(eNoeud);

        for (int i = 0; i < enfants.length; ++i) {
            jolie.afficherElementDebut(eCase,
                    new JolieAffichage.Attribut("i", "" + i)
            );
            if (null == enfants[i]) {
                jolie.afficherChaine("null");
            } else {
                enfants[i].jolieToString(jolie);
            }
            jolie.afficherElementFin(eCase);
        }

        jolie.afficherElementFin(eNoeud);
    }

    @Override
    public String toString() {
        JolieAffichage jolie = new JolieAffichage();
        jolieToString( jolie );

        return jolie.resultat();
    }

    /**
     Vérifie si un élément donné appartient à l'arbre.
     @param element L'élément à vérifier.
     @return {@code true} si l'élément appartient à l'arbre, {@code false} sinon.
     */
    public boolean appartient(int element) {
        int ratio = enfants.length;
        NoeudAbstrait enfant = enfants[element%ratio];
        if (enfant == null){
            return false;
        } else {
            if (enfant instanceof Feuille){
                return ((Feuille) enfant).element == element/ratio;
            } else {
                return ((Noeud)enfant).appartient(element/ratio);
            }
        }
    }

    /**
     Insère un élément donné dans l'arbre.
     @param element L'élément à insérer.
     */
    public void inserer(int element) {
        int ratio = enfants.length;

        if (enfants[element%ratio] == null){
            enfants[element%ratio] = new Feuille(element/ratio);
        } else {
            if (enfants[element%ratio] instanceof Feuille){
                int elementEnfant = ((Feuille) enfants[element%ratio]).element;
                enfants[element%ratio] = new Noeud(ratio);
                ((Noeud) enfants[element%ratio]).enfants[elementEnfant%ratio] = new Feuille(elementEnfant/ratio);

                ((Noeud) enfants[element%ratio]).inserer(element/ratio);

            } else {
                ((Noeud)enfants[element%ratio]).inserer(element/ratio);
            }
        }
    }

}
