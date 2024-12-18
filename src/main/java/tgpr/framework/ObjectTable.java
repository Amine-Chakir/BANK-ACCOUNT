package tgpr.framework;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import tgpr.bank.model.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Ce composant présente le même comportement que le composant {@code Table<T>} de <b>lanterna</b>, dont il hérite.
 * Il permet cependant de présenter de manière tabulaire des objets de type {@code T}.</p>
 *
 * <p>Chaque ligne correspond à un objet et les colonnes correspondent à des données de l'objet courant récupérées à
 * partir de fonctions anonymes.</p>
 *
 * <p>Lors de l'instanciation du composant, on doit lui passer une liste de {@link ColumnSpec} qui vont permettre de
 * définir les caractéristiques des différentes colonnes : titre, largeur, référence vers une méthode permettant de
 * récupérer la valeur, ...</p>
 *
 * <p>En interne, le composant utilise un {@code Table<String>}. Cela signifie que chaque cellule affiche le contenu
 * d'un {@code String} dont la valeur est récupérée en appelant la méthode référencée dans la spécification de la
 * colonne correspondante et en lui passant l'objet courant en paramètre.</p>
 *
 * <p>Voici un exemple d'utilisation :</p>
 * <pre>{@code
 * var tbl = ObjectTable<Person>(
 *     new ColumnSpec<>("Id", p -> p.getId()).format("%04d"),
 *     new ColumnSpec<>("Name", Person::getName).setWidth(20)
 * );
 * ...
 * tbl.clear();
 * tbl.add(new Person(1, "John"));
 * tbl.add(new Person(2, "Tom"));
 * }</pre>
 * @param <T> le type d'objets que l'on veut représenter dans ce tableau
 */
public class ObjectTable<T> extends Table<String> {

    private final ColumnSpec<T>[] columnSpecs;
    private final List<T> objects = new ArrayList<>();

    /**
     * Le constructeur permet de spécifier les caractéristiques des différentes colonnes à afficher.
     * @param columns la liste des spécifications des colonnes
     */
    @SafeVarargs
    public ObjectTable(ColumnSpec<T>... columns) {
        super(Arrays.stream(columns).map(ColumnSpec::formattedHeader).toArray(String[]::new));
        columnSpecs = columns;
    }

    /**
     * Permet de supprimer tout le contenu de la table. Il est nécessaire d'appeler cette méthode plutôt que celle
     * du {@code TableModel<T>} car le composant garde en interne une liste des objets affichés et cette liste doit
     * être vidée également.
     */
    public void clear() {
        getTableModel().clear();
        objects.clear();
    }

    private List<String> getValues(T obj) {
        var values = new ArrayList<String>();
        for (ColumnSpec<T> spec : columnSpecs) {
            var value = spec.computeValue(String.format(spec.getFormat(), spec.getGetter().apply(obj)));
            values.add(value);
        }
        return values;
    }

    /**
     * Permet d'ajouter un objet à afficher dans la table sous la forme d'une nouvelle ligne.
     * @param obj l'objet à ajouter
     */
    public void add(T obj) {
        objects.add(obj);
        getTableModel().addRow(getValues(obj));
    }

    /**
     * Permet d'ajouter une liste d'objets à afficher dans la table.
     * @param objects la liste d'objets à ajouter
     */
    public void add(List<T> objects) {
        for (var obj : objects)
            add(obj);
    }

    /**
     * Permet d'ajouter une liste explicite d'objets à afficher dans la table.
     * @param objects la liste d'objets à ajouter
     */
    @SafeVarargs
    public final void add(T... objects) {
        for (var obj : objects)
            add(obj);
    }

    /**
     * Permet d'insérer objet à une position (ligne) donnée dans la table.
     * @param index la position (ligne) d'insertion (indicée à partir de zéro)
     * @param obj l'objet à insérer
     */
    public void insert(int index, T obj) {
        objects.add(index, obj);
        getTableModel().insertRow(index, getValues(obj));
    }

    /**
     * Permet de récupèrer l'objet couramment sélectionné.
     * @return l'objet en question
     */
    public T getSelected() {
        int row = getSelectedRow();
        return row >= 0 && row < objects.size() ? objects.get(row) : null;
    }

    /**
     * Permet de sélectionner la ligne correspondant à un objet donné.
     * @param obj l'objet en question
     */
    public void setSelected(T obj) {
        int row = objects.indexOf(obj);
        if (row >= 0)
            setSelectedRow(row);
    }
    public void deleteFavourite(Account account){

    }
    /**
     * Permet d'ajouter le composant à un {@link Panel} existant.
     * @param panel le {@link Panel} dans lequel on veut ajouter la table
     * @return l'instance courante du composant
     */
    public ObjectTable<T> addTo(Panel panel) {
        super.addTo(panel);
        return this;
    }

    /**
     * Permet de définir l'action à exécuter quand on actionne la ligne courante (via "Enter" ou la barre d'espace).
     * @param selectAction l'action à exécuter
     * @return l'instance courante du composant
     */
    public ObjectTable<T> setSelectAction(Runnable selectAction) {
        super.setSelectAction(selectAction);
        return this;
    }
}
