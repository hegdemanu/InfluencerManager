package com.influencerManager.interfaces;

import java.util.Map;

/**
 * Interface representing an entity that can be analyzed for metrics.
 * 
 * @author Influencer Manager Team
 * @version 1.0
 */
public interface Analyzable {
    /**
     * Get performance metrics.
     * 
     * @return Map of metrics
     */
    Map<String, Object> getMetrics();
    
    /**
     * Generate a performance report.
     * 
     * @return The report as a string
     */
    String generateReport();
}