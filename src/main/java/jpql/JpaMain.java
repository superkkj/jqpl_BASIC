package jpql;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        //DB당 하나만 생성 됨. 웹서비스 시작할떄
        //데이터베이스 하나씩 묶여서 돌아가기 때문에 항상 생성해야됨 .
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // 로딩시점 1개만 만들어얃 된다. 설정정보 불러오기

        //고객의 요청이 들어올때마다 아래 로직 실행 쓰레드간 공유 X 사용하고 버리자 서
        //emf 통해서 작업해야된다 .
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team1 = new Team();
            team1.setName("TeamA");
            em.persist(team1);


            Team team2 = new Team();
            team2.setName("TeamB");
            em.persist(team2);


            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setTeam(team1);
            member1.setMemberType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(11);
            member2.setTeam(team1);
            member2.setMemberType(MemberType.ADMIN);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(12);
            member3.setTeam(team2);
            member3.setMemberType(MemberType.ADMIN);

            Member member4 = new Member();
            member4.setUsername("회원4");
            member4.setAge(13);
            member4.setTeam(team2);
            member4.setMemberType(MemberType.ADMIN);


            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1").getResultList();

            int resultcount =em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            em.clear();

            Member findMember = em.find(Member.class , member1.getId());
            System.out.println("count : " + resultcount);

            System.out.println("membber1.getAge() : " + findMember.getAge());
            System.out.println("membber1.getAge() : " + member2.getAge());
            System.out.println("membber1.getAge() : " + member3.getAge());

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
