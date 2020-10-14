package formatter.streamconverter;

public class StackUnit {
    public StackMarker marker;
    public int startsFromPosition;

    public StackUnit(StackMarker marker, int position) {
        this.marker = marker;
        this.startsFromPosition = position;
    }

    public StackUnit(StackMarker marker) {
        this.marker = marker;
    }
}
