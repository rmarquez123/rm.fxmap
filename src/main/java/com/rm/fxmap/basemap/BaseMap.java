
package com.rm.fxmap.basemap;

/**
 *
 * @author Ricardo Marquez
 */
public enum BaseMap {
  ESRI_STREET_MAP("https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/%d/%d/%d", "ESRI_World_Street_Map"), 
  ESRI_WORLD("https://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/%d/%d/%d", "ESRI_World_Imagery");
  private final String baseUrl;
  private final String subDirName;

  BaseMap(String baseUrl, String subDirName) {
    this.baseUrl = baseUrl;
    this.subDirName = subDirName;
  }

  public String getBaseUrl() {
    return this.baseUrl;
  }

  public String getSubDirName() {
    return this.subDirName;
  }
  
}
