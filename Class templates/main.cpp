#include <iostream>
#include <string>
#include "Top.hpp"

using namespace std;

template<typename T> void print(const T&);

class Person {
    double height;
    string name;
public:
    Person(double height_, string name_): height(height_), name(name_){}
    bool operator<(const Person& other) const {return this->height < other.height;}
    friend ostream& operator<<(ostream& out, const Person& person) {out << "[" << person.name << ", " << person.height << "]"; return out;}
};

int main(int argc, char** argv) {
    Top<int> top_int(3);
    top_int.add({6, -1, 3, 20});
    top_int.add(5);
    print(top_int); //Printing best 3: 5 6 20

    Top<Person> top_persons(3);
    top_persons.emplace(1.93, "Daniel");
    top_persons.emplace(1.55, "Pedro");
    top_persons.emplace(1.77, "Pier");
    top_persons.emplace(1.69, "Andreas");
    print(top_persons); //Printing 3 taller persons: "[Andreas, 1.69] [Pier, 1.77] [Daniel, 1.93]"
}

template<typename T> void print(const T& container) {
    for (auto& i : container)
        cout << i << " ";
    cout << endl;
}
