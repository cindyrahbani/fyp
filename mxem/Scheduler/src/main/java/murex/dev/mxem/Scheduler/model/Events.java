package murex.dev.mxem.Scheduler.model;

import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Events {
    public List<Event> events;

    public Set<String> getIds(){
        Set<String> ids = new HashSet<>();
        for(Event event : events){
            ids.add(event.getEventId());
        }
        return ids;
    }

    public void addWithoutDuplicates(List<Event> evts){
        Set<String> ids = getIds();
        for(Event event : evts){
            if(!ids.contains(event.getEventId())){
                this.events.add(event);
            }
        }
    }
}
