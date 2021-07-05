package club.w0sv.sl3.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A dialog which offers
 */
public abstract class BooleanDialog extends LateInitDialog {

    private String trueOption;
    private String falseOption;
    private boolean defaultOption;
    private JToolBar toolbar;
    private boolean result;

    public BooleanDialog(Window owner, String trueOption, String falseOption, boolean defaultOption) {
        super(owner);
        this.trueOption = trueOption;
        this.falseOption = falseOption;
        this.defaultOption = defaultOption;
        setModalityType(ModalityType.DOCUMENT_MODAL);
    }

    @Override
    protected void initializeContent() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createPrimaryContent(), BorderLayout.CENTER);
        getContentPane().add(createButtonBar(), BorderLayout.SOUTH);
    }

    protected abstract Container createPrimaryContent();

    protected JToolBar createButtonBar() {
        JToolBar toolbar = new JToolBar(SwingConstants.HORIZONTAL);
        toolbar.setFloatable(false);
        JButton trueButton = toolbar.add(new BooleanAction(this, trueOption, true));
        JButton falseButton = toolbar.add(new BooleanAction(this, falseOption, false));
        getRootPane().setDefaultButton(defaultOption ? trueButton : falseButton);
        return toolbar;
    }

    public boolean getResult() {
        return result;
    }

    private void setResult(boolean b) {
        result = b;
        logger.debug("dialog result set to {}", b);
        dispose();
    }

    private static class BooleanAction extends AbstractAction {
        private final BooleanDialog dialog;
        private final boolean value;

        public BooleanAction(BooleanDialog dialog, String label, boolean value) {
            super(label);
            this.dialog = dialog;
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                dialog.logger.info("user pressed \"{}\"", value ? dialog.trueOption : dialog.falseOption);
                dialog.setResult(value);
            }
            catch (Exception ex) {
                dialog.logger.error("error storing dialog result", ex);
                JOptionPane.showMessageDialog(dialog, ex.getLocalizedMessage(), "Uh Oh", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
