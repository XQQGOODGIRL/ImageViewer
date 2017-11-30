package Controller;

import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class TagsView implements Initializable{

    /**
     * Button for delete some particular tags.
     */
    @FXML
    Button Delete;

    /**
     * Button for add some particular tags.
     */
    @FXML
    Button Add;

    /**
     * TextField for input new tag.
     */
    @FXML
    TextField newTag;

    /**
     * ListView to show list of all tags.
     */
    @FXML
    ListView<String> listOfTags;

    private Controller controller;

    /**
     * Initialize TagsView.
     * @param location
     * URL location
     * @param resources
     * ResourceBundle resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Update data for any change in listOfTags.
     */
    private void initData(){
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    /**
     * Delete selected tags.
     * @throws IOException
     * When user delete Tag, there might be IOException.
     */
    public void DeleteSelectedTags() throws IOException {
        ObservableList<String> listForDelete = listOfTags.getSelectionModel().getSelectedItems();
        for (String tag : listForDelete) {
            TagManager.removeTag(tag);
            Map<File, Image> map = ImageManager.getImages();
            ArrayList<Image> images = new ArrayList<>(map.values());
            for (Image image: images){
                if (image.getCurrentTags().contains(tag)){
                    String oldName = image.getName() + image.getExtension();
                    ImageManager.deleteTag(image.getFile().getAbsolutePath(), tag);
                    System.out.println(oldName);
                    for (String key: Controller.nameToFile.keySet()){
                        if (Controller.nameToFile.get(key).equals(image.getFile())){
                            oldName = key;
                        }
                    }
                    if (Controller.nameToFile.containsKey(oldName)) {
                        String newName = image.getName() + image.getExtension();
                        Controller.nameToFile.remove(oldName);
                        Controller.nameToFile.put(newName, image.getFile());
                        controller.initData(oldName, newName);
                    }
                }
            }
        }
        initData();
    }

    /**
     * Pass a Controller into this.controller.
     * @param controller The controller we want to pass into.
     */
    void passController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Add new Tag into TagManager.
     * @throws IOException
     * When user add Tag, there might be IOException.
     */
    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            TagManager.addTag(input);
        }
        newTag.clear();
        initData();
    }
}
