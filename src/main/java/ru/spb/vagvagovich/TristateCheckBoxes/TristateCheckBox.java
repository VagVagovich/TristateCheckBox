package ru.spb.vagvagovich.TristateCheckBoxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;

/**
 * Чекбокс с 3мя состояниями
 * @author Валерий Голубев (на основе класса из инета)
 */
public class TristateCheckBox extends JCheckBox {

    /** Свойство: иконка неопределенного состояния */
    public static final String INDETERMINATED_ICON_CHANGED_PROPERTY = "indeterminatedIcon";
    /** Свойство: иконка неопределенного состояния в сфокусированном режиме*/
    public static final String FOCUS_INDETERMINATED_ICON_CHANGED_PROPERTY = "focusIndeterminatedIcon";
    /** Свойство: иконка неопределенного состояния в задизейбленом режиме */
    public static final String DISABLED_INDETERMINATED_ICON_CHANGED_PROPERTY = "disabledIndeterminatedIcon";

    public static final String DEVELOP_DIR_PATH = "/src/main/resources";

    /**
     * Статус чекбокса
     */
    public static final class State {

        /** Название статуса */
        private String statusName = "";

        /**
         * Создать статус чекбокса
         */
        private State() {
        }

        /**
         * Создать статус чекбокса
         * @param statusName - название статуса
         */
        private State(String statusName) {
            this.statusName = statusName;
        }

        @Override
        public String toString() {
            return statusName;
        }
    }
    /** Статус чекбокса */
    public static final State NOT_SELECTED = new State("NOT_SELECTED");
    /** Статус чекбокса */
    public static final State CHECKED = new State("CHECKED");
    /** Статус чекбокса */
    public static final State CROSSED = new State("CROSSED");

    /** Модель чекбокса с 3мя состояниями */
    private final TristateModel model;

    /** Иконка неопределенного состояния */
    private Icon indeterminatedIcon;
    /** Иконка неопределенного состояния во время наведения укзателя мыши*/
    private Icon indeterminatedFocusIcon;
    /** Иконка неопределенного состояния в отключенном состоянии */
    private Icon disabledIndeterminatedIcon;

    /**
     * Создать чекбокс с 3мя состояниями
     */
    public TristateCheckBox() {
        this(null);
    }

    /**
     * Создать чекбокс с 3мя состояниями
     * @param text - текст чекбокса
     */
    public TristateCheckBox(String text) {
        this(text, NOT_SELECTED);
    }

    /**
     * Создать чекбокс с 3мя состояниями
     * @param text - - текст чекбокса
     * @param initial - начальный статус чекбокса
     */
    public TristateCheckBox(String text, State initial) {
        super(text);

        // Add a listener for when the mouse is pressed and released
        super.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                TristateCheckBox.this.mousePressed();
            }

            public void mouseReleased(MouseEvent e) {
                TristateCheckBox.this.mouseReleased();
            }
        });
        // Reset the keyboard action map
        ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TristateCheckBox.this.mousePressed();
            }
        });
        map.put("released", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TristateCheckBox.this.mouseReleased();
            }
        });
        SwingUtilities.replaceUIActionMap(this, map);
        // set the model to the adapted model
        model = new TristateModel(getModel());
        setModel(model);
        setState(initial);
    }

    public String getUIClassID() {
        return "TristateCheckBoxUI";
    }

    /**
     * Обновить UI
     */
    public void updateUI() {
        setUI((ButtonUI)UIManager.getUI(this));
    }

    /**
     * обработать нажатие мыши
     */
    private void mousePressed() {
        grabFocus();
        model.setPressed(true);
        model.setArmed(true);
    }

    /**
     * Обработать выход мыши из чекбокса
     */
    private void mouseReleased() {
        model.nextState();
        model.setArmed(false);
        model.setPressed(false);
    }

    /**
     * Сделать клик
     */
    public void doClick() {
        mousePressed();
        mouseReleased();
    }

    //    /** No one may add mouse listeners, not even Swing! */
    //    public void addMouseListener(MouseListener l) {
    //    }

    /**
     * Задать статус чекбокса (CHECKED, CROSSED или NOT_SELECTED)
     * @param state - статус чекбокса
     */
    public void setState(State state) {
        model.setState(state);
    }

    /**
     * Вернуть статус чекбокса
     * @return статус чекбокса
     */
    public State getState() {
        return model.getState();
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            setState(CHECKED);
        } else {
            setState(NOT_SELECTED);
        }
    }

    /**
     * Получить иконку недетерменированного(неопределенного) состояния
     * @return иконка недетерменированного(неопределенного) состояния
     */
    public Icon getIndeterminatedIcon() {
        return indeterminatedIcon;
    }

    /**
     * Задать иконку недетерменированного(неопределенного) состояния
     * @param indeterminatedIcon -иконка недетерменированного(неопределенного) состояния
     */
    public void setIndeterminatedIcon(Icon indeterminatedIcon) {
        Icon oldValue = this.indeterminatedIcon;
        this.indeterminatedIcon = indeterminatedIcon;
        if (indeterminatedIcon != oldValue &&
                disabledIndeterminatedIcon instanceof UIResource) {
            disabledIndeterminatedIcon = null;
        }
        firePropertyChange(INDETERMINATED_ICON_CHANGED_PROPERTY, oldValue, indeterminatedIcon);
        if (accessibleContext != null) {
            accessibleContext.firePropertyChange(
                    AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                    oldValue, indeterminatedIcon);
        }
    }

    /**
     * Получить иконку недетерменированного(неопределенного) состояния
     * @return иконка недетерменированного(неопределенного) состояния
     */
    public Icon getIndeterminatedFocusIcon() {
        return indeterminatedFocusIcon;
    }

    /**
     * Задать иконку недетерменированного(неопределенного) состояния
     * @param indeterminatedFocusIcon -иконка недетерменированного(неопределенного) состояния
     */
    public void setIndeterminatedFocusIcon(Icon indeterminatedFocusIcon) {
        Icon oldValue = this.indeterminatedFocusIcon;
        this.indeterminatedFocusIcon = indeterminatedFocusIcon;
        //        if (indeterminatedFocusIcon != oldValue &&
        //            disabledIndeterminatedIcon instanceof UIResource) {
        //            disabledIndeterminatedIcon = null;
        //        }
        firePropertyChange(FOCUS_INDETERMINATED_ICON_CHANGED_PROPERTY, oldValue, indeterminatedFocusIcon);
        if (accessibleContext != null) {
            accessibleContext.firePropertyChange(
                    AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                    oldValue, indeterminatedFocusIcon);
        }
    }

    /**
     * Получить иконку недетерменированного(неопределенного) состояния
     * @return иконка недетерменированного(неопределенного) состояния
     */
    public Icon getDisabledIndeterminatedIcon() {
        if (disabledIndeterminatedIcon == null) {
            return getDisabledIcon();
        }
        return disabledIndeterminatedIcon;
    }

    /**
     * Задать иконку недетерменированного(неопределенного) состояния
     * @param disabledIndeterminatedIcon -иконка недетерменированного(неопределенного) состояния
     */
    public void setDisabledIndeterminatedIcon(Icon disabledIndeterminatedIcon) {
        Icon oldValue = this.disabledIndeterminatedIcon;
        this.disabledIndeterminatedIcon = disabledIndeterminatedIcon;
        firePropertyChange(DISABLED_INDETERMINATED_ICON_CHANGED_PROPERTY, oldValue, disabledIndeterminatedIcon);
        if (accessibleContext != null) {
            accessibleContext.firePropertyChange(
                    AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                    oldValue, disabledIndeterminatedIcon);
        }
        if (disabledIndeterminatedIcon != oldValue) {
            if (disabledIndeterminatedIcon == null || oldValue == null
                    || disabledIndeterminatedIcon.getIconWidth() != oldValue.getIconWidth()
                    || disabledIndeterminatedIcon.getIconHeight() != oldValue.getIconHeight()) {
                revalidate();
            }
        }
    }

    /**
     * Модель чекбокса с 3мя состояниями
     */
    private final class TristateModel implements ButtonModel {
        /** Исходная модель */
        private final ButtonModel other;
        /** Текущий статус */
        private State currentState = NOT_SELECTED;

        /**
         * Создать модель чексбокса с 3мя состояниями
         * @param other - исходная модель
         */
        private TristateModel(ButtonModel other) {
            this.other = other;
        }

        /**
         * Получить статус чекбокса
         * @return статус чекбокса
         */
        private State getState() {
            return currentState;
        }

        /**
         * Задать статус чекбокса
         * @param state - статус чекбокса
         */
        private void setState(State state) {
            this.currentState = state;
        }

        @Override
        public boolean isSelected() {
            return (currentState == CHECKED || currentState == CROSSED);
        }

        /**
         * We rotate between NOT_SELECTED, CHECKED and CROSSED
         */
        private void nextState() {
            State current = getState();
            if (current == NOT_SELECTED) {
                setState(CHECKED);
            } else if (current == CHECKED) {
                setState(CROSSED);
            } else if (current == CROSSED) {
                setState(NOT_SELECTED);
            }

            // This is to enforce a call to the fireStateChanged method
            other.setSelected(!other.isSelected());
        }

        @Override
        public void setArmed(boolean b) {
            other.setArmed(b);
        }

        /**
         * We disable focusing on the component when it is not enabled.
         */
        @Override
        public void setEnabled(boolean b) {
            setFocusable(b);
            other.setEnabled(b);
        }

        /**
         * All these methods simply delegate to the "other" model that is being decorated.
         */
        @Override
        public boolean isArmed() {
            return other.isArmed();
        }

        @Override
        public boolean isEnabled() {
            return other.isEnabled();
        }

        @Override
        public boolean isPressed() {
            return other.isPressed();
        }

        @Override
        public boolean isRollover() {
            return other.isRollover();
        }

        @Override
        public void setSelected(boolean b) {
            other.setSelected(b);
        }

        @Override
        public void setPressed(boolean b) {
            other.setPressed(b);
        }

        @Override
        public void setRollover(boolean b) {
            other.setRollover(b);
        }

        public void setMnemonic(int key) {
            other.setMnemonic(key);
        }

        @Override
        public int getMnemonic() {
            return other.getMnemonic();
        }

        @Override
        public void setActionCommand(String s) {
            other.setActionCommand(s);
        }

        @Override
        public String getActionCommand() {
            return other.getActionCommand();
        }

        @Override
        public void setGroup(ButtonGroup group) {
            other.setGroup(group);
        }

        @Override
        public void addActionListener(ActionListener l) {
            other.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            other.removeActionListener(l);
        }

        @Override
        public void addItemListener(ItemListener l) {
            other.addItemListener(l);
        }

        @Override
        public void removeItemListener(ItemListener l) {
            other.removeItemListener(l);
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            other.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            other.removeChangeListener(l);
        }

        @Override
        public Object[] getSelectedObjects() {
            return other.getSelectedObjects();
        }
    }

}
