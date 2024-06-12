package site;

import org.openqa.selenium.Rectangle;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class ComponentReducer {
    private Set<String> seenLocations;
    private Optional<Rectangle> overlappingComponent;

    public ComponentReducer() {
        this.seenLocations = new HashSet<>();
        this.overlappingComponent = Optional.empty();
    }

    public void addLocation(Rectangle component) {
        seenLocations.add(component.toString());
    }

    public boolean hasSeenLocation(Rectangle component) {
        return seenLocations.contains(component.toString());
    }

    public void setOverlappingComponent(Rectangle component) {
        this.overlappingComponent = Optional.of(component);
    }

    public Optional<Rectangle> getOverlappingComponent() {
        return overlappingComponent;
    }

    public ComponentReducer combine(ComponentReducer other) {
        this.seenLocations.addAll(other.seenLocations);
        if (this.overlappingComponent.isEmpty()) {
            this.overlappingComponent = other.overlappingComponent;
        }
        return this;
    }
}
