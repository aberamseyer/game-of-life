import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Transient;
import java.util.Random;

public class ConwaysGameOfLife extends JPanel {
    private boolean[][] grid;
    private Random rnd = new Random();
    private int generationCounter;

    public ConwaysGameOfLife(int height, int width) {
        this.grid = new boolean[width / 4][height / 4];

        // initialize grid
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid[i].length; j++) {
                if (rnd.nextDouble() < .1) {
                    this.grid[i][j] = true;
                }
            }
        }
    }

    public void updateGrid() {
        boolean[][] newGrid = new boolean[this.grid.length][this.grid[0].length];
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid[i].length; j++) {
                // calculate alive neighbors
                int aliveNeighbors = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (i + k >= 0 && i + k < this.grid.length &&
                                j + l >= 0 && j + l < this.grid[i + k].length &&
                                this.grid[i + k][j + l]
                        ) {
                            aliveNeighbors++;
                        }
                    }
                }

                // don't count ourselves as a neighbor
                if (this.grid[i][j]) {
                    aliveNeighbors--;
                }

                // initialize new grid to current grid state
                newGrid[i][j] = this.grid[i][j];

                // 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                if (this.grid[i][j] && aliveNeighbors < 2) {
                    newGrid[i][j] = false;
                }
                // 2. Any live cell with two or three live neighbours lives on to the next generation.
                else if (this.grid[i][j] && aliveNeighbors <= 3) {
                    // stay alive
                }
                // 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
                else if (this.grid[i][j] && aliveNeighbors > 3) {
                    newGrid[i][j] = false;
                }
                // 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                else if (!this.grid[i][j] && aliveNeighbors == 3) {
                    newGrid[i][j] = true;
                }
            }
        }


        // copy new grid to old grid
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = newGrid[i][j];
            }
        }
    }

    @Override
    @Transient
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * 4, grid[0].length * 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Generation: " + generationCounter++, 0, 10);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                g.setColor(this.grid[i][j] ? Color.green : Color.white);
                g.fillRect(j*4, i*4, 4, 4);
            }
        }
    }


    public static void main(String[] args) {
        final ConwaysGameOfLife c = new ConwaysGameOfLife(800, 800);
        JFrame frame = new JFrame();
        frame.getContentPane().add(c);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.updateGrid();
                c.repaint();
            }
        }).start();
    }
}
