package org.webonise.ameya.scratchpad.Image;


import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javafx.embed.swing.SwingNode;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class OpenGlPanel extends SwingNode implements GLEventListener,
        MouseWheelListener, ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private static final int FPS = 60;
    private static final float DEFAULT_SCALE = 1.25f;

    private GLJPanel gljPanel = new GLJPanel();

    private Point lastMousePos;
    private int screenHeight;
    private int screenWidth;
    private float scale;
    private float cameraZ = 0.0f;
    private float cameraX = 0.0f;
    private float cameraY = 0.0f;
    private boolean textureRefreshRequired = true;
    private Texture texture;
    private FPSAnimator animator;
    private boolean showGrid;

    public void initialize() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        gljPanel = new GLJPanel(glCapabilities);
        gljPanel.addGLEventListener(this);
        gljPanel.addMouseWheelListener(this);
        gljPanel.addMouseListener(this);
        gljPanel.addMouseMotionListener(this);
        gljPanel.addKeyListener(this);

        animator = new FPSAnimator(gljPanel, FPS, true);
        setContent(gljPanel);

        animator.start();
        scale = DEFAULT_SCALE;
    }



    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glEnable(GL2.GL_LINE_SMOOTH);
        gl2.glEnable(GL2.GL_POINT_SMOOTH);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl2.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
        gl2.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST);

        gl2.glShadeModel(GL2.GL_SMOOTH);
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glDepthFunc(GL2.GL_LEQUAL);
        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        gl2.glEnable(GL2.GL_TEXTURE_2D);

        gl2.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);

        setTexture(gl2);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl2.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();

        screenWidth = glAutoDrawable.getWidth();
        screenHeight = glAutoDrawable.getHeight();

        if (textureRefreshRequired) {
            setTexture(gl2);
            textureRefreshRequired = false;
        }

        orientCamera(gl2, screenWidth, screenHeight);

        float textureAspect = texture.getAspectRatio();
        float txWidth = textureAspect < 1 ? 0.5f : textureAspect / 2.0f;
        float txHeight = textureAspect < 1 ? textureAspect / 2.0f  : 0.5f;

        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureObject());
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glBegin( GL2.GL_QUADS );
        gl2.glTexCoord4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl2.glVertex3f(-txWidth, txHeight, 0);

        gl2.glTexCoord4f(0.0f, 0.0f, 0.0f, 1.0f);
        gl2.glVertex3f(-txWidth, -txHeight, 0);

        gl2.glTexCoord4f(1.0f, 0.0f, 0.0f, 1.0f);
        gl2.glVertex3f(txWidth, -txHeight, 0);

        gl2.glTexCoord4f(1.0f, 1.0f, 0.0f, 1.0f);
        gl2.glVertex3f(txWidth, txHeight, 0);
        gl2.glEnd();

        if (showGrid) {
            drawGrid(gl2);
        }
    }

    private void drawGrid(GL2 gl2) {
        float dim = 20;
        float increment = 0.1f;
        float z = 0.2f;
        gl2.glDisable(GL.GL_TEXTURE_2D);
        gl2.glColor3f(0.0f, 0.0f, 0.0f);
        for (float x = -dim; x < dim; x += increment) {
            // Vertical line
            gl2.glBegin(GL2.GL_LINES);
            gl2.glVertex3f(x, -dim, z);
            gl2.glVertex3f(x, dim, z);
            gl2.glEnd();


            // Horizontal line
            gl2.glBegin(GL2.GL_LINES);
            gl2.glVertex3f(-dim, x, z);
            gl2.glVertex3f(dim, x, z);
            gl2.glEnd();
        }
    }

    private void orientCamera(GL2 gl2, int screenWidth, int screenHeight) {
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        float aspect = (float)screenWidth / (float)screenHeight;
        float maxDim = 0.75f;

        if (aspect > 1.0) {
            gl2.glOrtho(-maxDim * aspect, maxDim * aspect, -maxDim, maxDim, 1.0, -1.0);

        } else {
            gl2.glOrtho(-maxDim, maxDim, -maxDim / aspect, maxDim / aspect, 1.0, -1.0);
        }

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glEnable(GL2.GL_DEPTH_TEST);

        gl2.glScaled(scale, scale, scale);
        gl2.glTranslatef(cameraX, cameraY, cameraZ);
    }

    private void setTexture(GL gl) {
        try {
            if (texture!=null) {
                texture.destroy(gl);
            }
            texture = TextureIO.newTexture(new File(getClass().getResource("/images/DSC_2075.JPG").getFile()), false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width,
                        int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width , 0.0f, height);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double delta = e.getPreciseWheelRotation();
        if (delta < 0 && scale < 5) {
            zoomIn();
        } else if (delta > 0) {
            zoomOut();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        gljPanel.getContext().makeCurrent();
    }

    public void pauseImagePanelView(){
        animator.stop();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("x : " + x + " y : " + y + " screenWidth : " + screenWidth);
        System.out.println("x' : " + scale * (x + (screenWidth / 2)));
        System.out.println("y' : " + scale * ((-1 * y) + (screenWidth / 2)));
    }

    public void mousePressed(MouseEvent e) {
        gljPanel.grabFocus();
    }

    public void mouseDragged(MouseEvent e) {
        float scrollDampener = 300.0f;
        if(lastMousePos != null) {
            cameraX += (e.getX() - lastMousePos.getX()) / scrollDampener;
            cameraY -= (e.getY() - lastMousePos.getY()) / scrollDampener;
        }
        lastMousePos = e.getPoint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                //selectNext();
                break;
            case KeyEvent.VK_LEFT:
                //selectPrevious();
                break;
            case KeyEvent.VK_UP:
                zoomIn();
                break;
            case KeyEvent.VK_DOWN:
                zoomOut();
                break;
            case KeyEvent.VK_G:
                showGrid = !showGrid;
                break;
        }
    }

    public void zoomIn(){
        if(scale < 5){
            scale += 0.1;
        }
    }

    public void zoomOut(){
        scale -= 0.1;
        scale = Math.max(scale, 1.0f);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        lastMousePos = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
