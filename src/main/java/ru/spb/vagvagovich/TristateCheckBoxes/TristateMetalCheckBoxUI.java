package ru.spb.vagvagovich.TristateCheckBoxes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicCheckBoxUI;


import ru.spb.nicetu.oestik.utils.ResourcePathUtil;

import sun.swing.SwingUtilities2;

/**
 * Внешний вид(UI) чекбокса с 3мя состояниями
 * @author Валерий Голубев
 */
public class TristateMetalCheckBoxUI extends BasicCheckBoxUI {
    /**
     * Экземпляр пользовательского интерфейса
     */
    protected static final TristateMetalCheckBoxUI TRISTATE_CHECKBOX_UI = new TristateMetalCheckBoxUI();

    /** Путь к иконке приложения */
    private static final String INDETERMINATE_ICON_PATH = "/imgs/check_indeterm.png";
    /** Путь к иконке приложения */
    private static final String INDETERMINATE_SELECTED_ICON_PATH = "/imgs/check_indeterm_selected.png";


    /**
     * Возвращает пользовательский интерфейс
     * @param b - компонент
     * @return checkboxUI
     */
    public static ComponentUI createUI(JComponent b) {
        return TRISTATE_CHECKBOX_UI;
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        if (b instanceof TristateCheckBox) {
            TristateCheckBox checkBox= (TristateCheckBox) b;
            if (checkBox.getIndeterminatedIcon() == null || checkBox.getIndeterminatedIcon() instanceof UIResource) {
                Icon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        ResourcePathUtil.getPath(INDETERMINATE_ICON_PATH, TristateCheckBox.DEVELOP_DIR_PATH, true,
                                TristateMetalCheckBoxUI.class)));
                checkBox.setIndeterminatedIcon(icon);
            }
            if (checkBox.getIndeterminatedFocusIcon() == null || checkBox.getIndeterminatedFocusIcon() instanceof UIResource) {
                Icon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        ResourcePathUtil.getPath(INDETERMINATE_SELECTED_ICON_PATH, TristateCheckBox.DEVELOP_DIR_PATH, true,
                                TristateMetalCheckBoxUI.class)));
                checkBox.setIndeterminatedFocusIcon(icon);
            }
            if (checkBox.getDisabledIndeterminatedIcon() == null || checkBox.getDisabledIndeterminatedIcon() instanceof UIResource) {
                Icon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        ResourcePathUtil.getPath(INDETERMINATE_ICON_PATH, TristateCheckBox.DEVELOP_DIR_PATH, true,
                                TristateMetalCheckBoxUI.class)));
                checkBox.setDisabledIndeterminatedIcon(icon);
            }
        }
    }

    @Override
    public synchronized void paint(Graphics g, JComponent c) {
        if (c instanceof TristateCheckBox) {
            super.paint(g, c);
            TristateCheckBox checkBox= (TristateCheckBox) c;
            ButtonModel model = checkBox.getModel();
            Icon tmpIcon = null;
            if (checkBox.getState() == TristateCheckBox.CROSSED && !checkBox.isEnabled()) {
                tmpIcon = checkBox.getDisabledIndeterminatedIcon();
            } else if (checkBox.getState() == TristateCheckBox.CROSSED
                    && checkBox.isEnabled() && checkBox.isRolloverEnabled() && model.isRollover()) {
                tmpIcon = checkBox.getIndeterminatedFocusIcon();
            } else if (checkBox.getState() == TristateCheckBox.CROSSED && checkBox.isEnabled()) {
                tmpIcon = checkBox.getIndeterminatedIcon();
            }
            if (tmpIcon != null) {
                Font f = c.getFont();
                g.setFont(f);
                FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

                Rectangle viewRect = new Rectangle();
                Rectangle iconRect = new Rectangle();
                Rectangle textRect = new Rectangle();

                Insets i = checkBox.getInsets();
                Dimension size = checkBox.getSize();
                viewRect.x = i.left;
                viewRect.y = i.top;
                viewRect.width = size.width - (i.right + viewRect.x);
                viewRect.height = size.height - (i.bottom + viewRect.y);
                iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
                textRect.x = textRect.y = textRect.width = textRect.height = 0;

                SwingUtilities.layoutCompoundLabel(
                        c, fm, checkBox.getText(), tmpIcon,
                        checkBox.getVerticalAlignment(), checkBox.getHorizontalAlignment(),
                        checkBox.getVerticalTextPosition(), checkBox.getHorizontalTextPosition(),
                        viewRect, iconRect, textRect,
                        checkBox.getText() == null ? 0 : checkBox.getIconTextGap());

                tmpIcon.paintIcon(c, g, iconRect.x, iconRect.y);
            }
        } else {
            super.paint(g, c);
        }
    }
}
