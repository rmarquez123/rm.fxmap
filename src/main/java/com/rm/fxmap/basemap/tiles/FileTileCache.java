package com.rm.fxmap.basemap.tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author rmarquez
 */
public class FileTileCache implements TileCache {

  private final File baseDir;
  private final Property<String> source = new SimpleObjectProperty<>();

  /**
   *
   * @param baseDir The base directory of where to store tiles.
   * @param source The source name which is based on the base tile map type.
   * This will be used as the subfolder name.
   */
  public FileTileCache(File baseDir, String source) {
    this.baseDir = baseDir;
    this.source.setValue(source);
    Logger.getLogger(FileTileCache.class.getName())
            .log(Level.INFO, "Map tiles wil be saved at : ''{0}''", this.baseDir);
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public boolean containsKey(TileIndices key) {
    File file = this.getFile(key);
    boolean result = file.exists();
    return result;
  }

  private File getFile(TileIndices key) {
    String keyToFile = key.level + File.separator + key.col + File.separator + key.row + ".png";
    File result = new File(this.baseDir + File.separator + this.source.getValue(), keyToFile);

    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public Tile get(TileIndices key) {
    Tile r = new Tile() {
      @Override
      protected Image onLoadImage() {
        File file = getFile(key);
        InputStream is;
        try {
          is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
          throw new RuntimeException(ex);
        }
        Image result = new Image(is);
        return result;
      }
    };
    r.loadImage();
    return r;
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void put(TileIndices key, Tile tile) {
    File file = this.getFile(key);
    Image image = tile.getImage().getValue();
    if (image != null) {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      try {
        BufferedImage x = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(x, "png", file);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public String toString() {
    return "FileTileCache{" + "baseDir=" + baseDir + '}';
  }

}
