import java.util.ArrayList;
import java.util.List;

enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED
}

abstract class Employee {
    private String id;
    private String name;

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean canApplyLeave(int days);
}

class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean canApplyLeave(int days) {
        return days <= 30;
    }
}

class PartTimeEmployee extends Employee {
    public PartTimeEmployee(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean canApplyLeave(int days) {
        return days <= 10;
    }
}

class LeaveRequest {
    private Employee employee;
    private String startDate;
    private String endDate;
    private int durationDays;
    private LeaveStatus status;

    public LeaveRequest(Employee employee, String startDate, String endDate, int durationDays) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationDays = durationDays;
        this.status = LeaveStatus.PENDING;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public String getPeriod() {
        return startDate + " to " + endDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public boolean setStatus(LeaveStatus newStatus) {
        if (this.status == LeaveStatus.APPROVED || this.status == LeaveStatus.REJECTED) {
            System.out.println("Cannot change leave request status from " + this.status + " to " + newStatus + ".");
            return false;
        }
        this.status = newStatus;
        return true;
    }
}

class Manager {
    private String name;

    public Manager(String name) {
        this.name = name;
    }

    public void reviewRequest(LeaveRequest request, boolean approve) {
        if (!request.getEmployee().canApplyLeave(request.getDurationDays())) {
            System.out.println("Leave request exceeds maximum allowed limit for " + request.getEmployee().getName() + ".");
            request.setStatus(LeaveStatus.REJECTED);
            return;
        }

        if (approve) {
            if (request.setStatus(LeaveStatus.APPROVED)) {
                System.out.println(request.getEmployee().getName() + "'s leave request (" + 
                                   request.getPeriod() + ") approved. Status: " + request.getStatus() + ".");
            }
        } else {
            if (request.setStatus(LeaveStatus.REJECTED)) {
                System.out.println(request.getEmployee().getName() + "'s leave request (" + 
                                   request.getPeriod() + ") rejected. Status: " + request.getStatus() + ".");
            }
        }
    }
}

public class MainLeaveRequest {
    public static void main(String[] args) {
        Employee john = new FullTimeEmployee("E001", "John");
        Employee jane = new PartTimeEmployee("E002", "Jane");

        Manager alice = new Manager("Alice");
        Manager bob = new Manager("Bob");

        LeaveRequest request1 = new LeaveRequest(john, "Jan 1", "Jan 5", 5);
        System.out.println("Leave request submitted for " + john.getName() + " (" + request1.getPeriod() + "). Status: " + request1.getStatus() + ".");
        alice.reviewRequest(request1, true);

        LeaveRequest request2 = new LeaveRequest(jane, "Feb 10", "Feb 11", 2);
        System.out.println("Leave request submitted for " + jane.getName() + " (" + request2.getPeriod() + "). Status: " + request2.getStatus() + ".");
        bob.reviewRequest(request2, false);

        request1.setStatus(LeaveStatus.PENDING);
    }
}