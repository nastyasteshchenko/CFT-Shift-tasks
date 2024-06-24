package focus.start.task5.production;

class IdGenerator {
    private long id = 0;

    long getId() {
        return id++;
    }
}
