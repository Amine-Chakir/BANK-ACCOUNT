package tgpr.framework;

import java.util.function.Function;

/**
 * Permet de spécifier les attributs d'une colonne d'un composant de type {@link ObjectTable}.
 * @param <T> le type d'objet qui sera affiché dans la table
 */
public class ColumnSpec<T> {
    /**
     * Les modes d'alignement possibles du texte d'une colonne.
     */
    public enum Alignment {
        Left, Right
    }

    /**
     * Les modes de gestion d'un débordement du texte à afficher par rapport à la largeur maximale imposée pour la colonne.
     */
    public enum OverflowHandling {
        /**
         * Le texte sera abrégé, si nécessaire, pour tenir dans la largeur de la colonne. Dans le cas où il est abrégé,
         * on lui concatène {@code "..."} pour indiquer qu'il a été abrégé.
         */
        Abbreviate,
        /**
         * La partie du texte qui dépasse la largeur de la colonne est tout simplement ignoré.
         */
        Hide,
        /**
         * Le texte est présenté sur plusieurs lignes, chaque ligne respectant la largeur imposée.
         */
        Wrap
    }

    private final String header;
    private final Function<T, Object> getter;
    private String format = "%s";
    private int minWidth = 0;
    private int maxWidth = 0;
    private Alignment alignment = Alignment.Left;
    private OverflowHandling overflowHandling = OverflowHandling.Abbreviate;

    /**
     * Permet de construire une spécification d'une colonne.
     * @param header le titre à afficher pour la colonne
     * @param getter une référence vers une méthode qui permet de récupérer une valeur à afficher ; cette méthode prend
     *               un objet de type {@code T} en paramètre et retourne un {@link Object} sur lequel on appliquera la
     *               méthode {@code toString()} pour récupérer la chaîne de caractère à afficher
     */
    public ColumnSpec(String header, Function<T, Object> getter) {
        this.header = header;
        this.getter = getter;
    }

    public String getHeader() {
        return header;
    }

    public Function<T, Object> getGetter() {
        return getter;
    }

    /**
     * Permet de récupérer le format associé à la colonne (voir {@link #setFormat})
     * @return le format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Permet de spécifier un format d'affichage qui sera appliqué à la valeur reçue du {@code getter} pour la transformer
     * en {@code String}. Ce format doit correspondre aux formats gérés par {@link String#format}.
     * @param format le format
     * @return l'instance courante
     */
    public ColumnSpec<T> setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Voir {@link #setMinWidth}.
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * Permet de définir la largeur minimale de la colonne.
     * @param minWidth la largeur à prendre en compte
     * @return l'instance courante
     */
    public ColumnSpec<T> setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    /**
     * Voir {@link #setMaxWidth}.
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Permet de définir la largeur maximale de la colonne.
     * @param maxWidth la largeur à prendre en compte
     * @return l'instance courante
     */
    public ColumnSpec<T> setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * Permet d'imposer une largeur fixe pour la colonne. Equivalent à donner la même valeur pour {@code minWidth} et
     * {@code maxWidth}.
     * @param width la largeur à prendre en compte
     * @return l'instance courante
     */
    public ColumnSpec<T> setWidth(int width) {
        this.minWidth = width;
        this.maxWidth = width;
        return this;
    }

    /**
     * Voir {@link #setAlignment}.
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Permet d'indiquer comment doit se faire l'alignement du texte pour la colonne (voir {@link Alignment}).
     * @param alignment l'alignement à prendre en compte
     * @return l'instance courante
     */
    public ColumnSpec<T> setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    /**
     * Permet d'imposer un alignement à gauche (voir {@link Alignment}).
     * @return l'instance courante
     */
    public ColumnSpec<T> alignLeft() {
        this.alignment = Alignment.Left;
        return this;
    }

    /**
     * Permet d'imposer un alignement à droite (voir {@link Alignment}).
     * @return l'instance courante
     */
    public ColumnSpec<T> alignRight() {
        this.alignment = Alignment.Right;
        return this;
    }

    /**
     * Voir {@link #setOverflowHandling}.
     */
    public OverflowHandling getOverflowHandling() {
        return overflowHandling;
    }

    /**
     * Permet de définir le mode de gestion du dépassement de la largeur (voir {@link OverflowHandling}).
     * @return l'instance courante
     */
    public ColumnSpec<T> setOverflowHandling(OverflowHandling overflowHandling) {
        this.overflowHandling = overflowHandling;
        return this;
    }

    protected String computeValue(String value) {
        if (minWidth > 0 && value.length() < minWidth) {
            String blancs = " ".repeat(minWidth - value.length());
            if (alignment == Alignment.Left)
                value = value + blancs;
            else if (alignment == Alignment.Right)
                value = blancs + value;
        }
        if (maxWidth > 0 && value.length() > maxWidth)
            if (overflowHandling == OverflowHandling.Abbreviate)
                value = Tools.abbreviate(value, Math.max(maxWidth - 3, 0));
            else if (overflowHandling == OverflowHandling.Wrap)
                value = Tools.wrap(value, Math.max(maxWidth - 3, 0));
            else if (overflowHandling == OverflowHandling.Hide)
                value = value.substring(0, maxWidth);
        return value;
    }

    protected String formattedHeader() {
        return computeValue(header);
    }
}
