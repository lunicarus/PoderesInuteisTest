package site;

import org.openqa.selenium.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class ComponentReducer {
    private Set<Rectangle> seenLocations = new HashSet<>();
    private Rectangle overlappingComponent;

    public void addLocation(Rectangle component) {
        seenLocations.add(component);
    }

    public boolean hasSeenLocation(Rectangle component) {
        return seenLocations.contains(component);
    }

    public void setOverlappingComponent(Rectangle component) {
        for(Rectangle knownComponent : seenLocations) {
            if (knownComponent.equals(component)) {
                this.overlappingComponent = component;
                break;
            }
            if(isOverlaping(component, knownComponent)) {
                this.overlappingComponent = component;
            }

        }

    }

    private static boolean isOverlaping(Rectangle component, Rectangle knownComponent) {
        return knownComponent.x < component.x + component.width &&
                knownComponent.x + knownComponent.height > component.x &&
                knownComponent.y < component.y + component.height &&
                knownComponent.y + knownComponent.height > component.y;
    }

    public Optional<Rectangle> getOverlappingComponent() {
        return Optional.ofNullable(overlappingComponent);
    }

    public ComponentReducer combine(ComponentReducer other) {
        this.seenLocations.addAll(other.seenLocations);
        if (this.overlappingComponent == null) this.overlappingComponent = other.overlappingComponent;
        return this;
    }
}
