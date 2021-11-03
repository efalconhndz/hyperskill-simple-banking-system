class Person {
    String name;
    int age;
}

class MakingChanges {
    public static void changeIdentities(Person p1, Person p2) {
        String tempName = p1.name;
        int tempage = p1.age;

        p1.name = p2.name;
        p1.age = p2.age;
        p2.name = tempName;
        p2.age = tempage;
    }
}