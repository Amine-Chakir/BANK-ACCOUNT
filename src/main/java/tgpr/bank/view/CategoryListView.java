package tgpr.bank.view;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import tgpr.bank.model.Account;
import tgpr.framework.ColumnSpec;
import tgpr.framework.ObjectTable;
import tgpr.framework.Tools;
import tgpr.framework.ViewManager;
import tgpr.bank.controller.CategoryListController;
import tgpr.bank.model.Category;

import java.awt.*;
import java.util.List;

public class CategoryListView extends BasicWindow {

    private final TextBox txtName;

    private final Button btnAddUpdate;
    private final Category category;
    private final Label errName;




    private  Account account;
    private final CategoryListController controller;
    private final ObjectTable<Category> table;

    public CategoryListView(CategoryListController controller,Category category) {
        super((category == null ? "Add " : "Update ") + "Category");
        this.controller = controller;
        this.category = category;

        setTitle("Categories");
        setHints(List.of(Hint.CENTERED));




        // Le panel 'root' est le composant racine de la fenêtre (il contiendra tous les autres composants)
        Panel root = new Panel();

        setComponent(root);

        // ajoute une ligne vide
        new EmptySpace().addTo(root);

        // crée un tableau de données pour l'affichage des membres
        table = new ObjectTable<>(
                new ColumnSpec<>("Name", Category::getName),
                new ColumnSpec<>("Type", m ->m.isAccount() ? "local" : "system")
                //new ColumnSpec<>("Uses", m->Tools.ifNull(m.getUses(),"0"))
        );
        root.addComponent(table);
        table.setPreferredSize(new TerminalSize(ViewManager.getTerminalColumns(), 10));
        table.setSelectAction(() -> {
            var categorie = table.getSelected();
            //controller.e(categorie);
            reloadData();
            table.setSelected(categorie);
        });
        reloadData();

        //setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        // permet de fermer la fenêtre en pressant la touche Esc
       // setCloseWindowWithEscape(true);
        // définit une taille fixe pour la fenêtre de 15 lignes et 70 colonnes
        //setFixedSize(new TerminalSize(70, 15));


        new EmptySpace().addTo(root);
        new Label("").addTo(root);
        txtName = new TextBox(new TerminalSize(21, 1)).addTo(root)
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(root);
        errName = new Label("").addTo(root).setForegroundColor(TextColor.ANSI.RED);
           // txtCategory = new TextBox(new TerminalSize(12, 1)).addTo(root).setTextChangeListener((txt, byUser) -> validate());
        var buttons = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        btnAddUpdate = new Button(category == null ? "Add" : "Update", this::add).addTo(buttons);
        new Button("Reset", this::close).addTo(buttons);
        setComponent(root);

        if (category != null) {
            txtName.setText(category.getName());
        }

    }



    public void reloadData() {
        // vide le tableau
        table.clear();
        // demande au contrôleur la liste des membres
        var category = controller.getCategory();
        // ajoute l'ensemble des membres au tableau
        table.add(category);
    }

    private void add() {
        /*controller.save(
                txtName.getText(),
                category.getId()
        );*/
    }

    private void validate() {
    }

}
