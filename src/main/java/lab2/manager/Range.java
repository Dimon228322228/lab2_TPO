package lab2.manager;

/**
 * Immutable range definition for function evaluation.
 * Represents a closed interval [start, end] with a step size.
 * 
 * @param start inclusive start of the range
 * @param end inclusive end of the range
 * @param step positive step size between points
 */
public record Range(double start, double end, double step) {
    
    /**
     * Validates the range parameters.
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Range {
        if (start >= end) {
            throw new IllegalArgumentException("Start must be less than end. Got: start=" + start + ", end=" + end);
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be positive. Got: " + step);
        }
        if (Double.isNaN(start) || Double.isNaN(end) || Double.isNaN(step)) {
            throw new IllegalArgumentException("Range parameters cannot be NaN");
        }
        if (Double.isInfinite(start) || Double.isInfinite(end) || Double.isInfinite(step)) {
            throw new IllegalArgumentException("Range parameters cannot be infinite");
        }
    }
    
    /**
     * Returns the number of points in this range (inclusive of both start and end).
     * The count is calculated as floor((end - start) / step) + 1.
     * 
     * @return number of points, at least 2
     */
    public int pointCount() {
        return (int) Math.floor((end - start) / step) + 1;
    }

    /**
     * Creates a new range with the same bounds but different step.
     * 
     * @param newStep new step value
     * @return new Range with updated step
     */
    public Range withStep(double newStep) {
        return new Range(start, end, newStep);
    }
    
    @Override
    public String toString() {
        return String.format("Range[start=%.4f, end=%.4f, step=%.4f, points=%d]", 
            start, end, step, pointCount());
    }
}