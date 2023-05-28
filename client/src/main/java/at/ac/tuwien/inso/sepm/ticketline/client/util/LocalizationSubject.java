package at.ac.tuwien.inso.sepm.ticketline.client.util;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class LocalizationSubject {

    private List<LocalizationObserver> localizationObservers = new LinkedList<>();

    public void attach(LocalizationObserver localizationObserver){
        localizationObservers.add(localizationObserver);
    }

    public void notifyAllObservers(){
        localizationObservers.forEach(LocalizationObserver::update);
    }

    public void detach(LocalizationObserver localizationObserver){ localizationObservers.remove(localizationObserver); }
}
