package com.influencerManager.interfaces;

/**
 * Interface representing an entity that can be managed (started, paused, etc.).
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public interface Manageable {
    /**
     * Start the entity.
     */
    void start();
    
    /**
     * Pause the entity.
     */
    void pause();
    
    /**
     * Resume the entity.
     */
    void resume();
    
    /**
     * End the entity.
     */
    void end();
    
    /**
     * Cancel the entity.
     */
    void cancel();
}