package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
/*
 * when console testing plugins :-
 * 
 * the plugin needs to be tested in a Pipeline
 * ChordProgrammer2, ChordForm, RhythmBuffer need to be constructed seperately and assigned to the Pipeline, otherwise it will look for a DAPlay
 * the plugin must only make calls to the Pipeline parent to access these objects
 * no MaxObject.post calls directly from the plugin. Still have not implemented a failsafe way of doing this, but 
 * will update this doc when i do......
 * 
 * 
 */
public class TestingPlugins {

}
