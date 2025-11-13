using UnityEngine;

public class BombTower : MonoBehaviour
{
    [Header("General")]
    public float range = 15f;
    public string enemyTag = "Enemy";

    [Header("Bullet Settings")]
    public GameObject bombPrefab;
    public float fireRate = 1f;
    public int damage = 50;
    public float explosionRadius = 3f;

    [Header("Unity Setup")]
    public Transform partToRotate;
    public float turnSpeed = 10f;
    public Transform firePoint;

    private Transform target;
    private float fireCountdown = 0f;

    void Start()
    {
        InvokeRepeating("UpdateTarget", 0f, 0.5f);
    }

    void UpdateTarget()
    {
        GameObject[] enemies = GameObject.FindGameObjectsWithTag(enemyTag);
        float shortestDistance = Mathf.Infinity;
        GameObject nearestEnemy = null;

        foreach (GameObject enemy in enemies)
        {
            float distanceToEnemy = Vector3.Distance(transform.position, enemy.transform.position);
            if (distanceToEnemy < shortestDistance)
            {
                shortestDistance = distanceToEnemy;
                nearestEnemy = enemy;
            }
        }

        if (nearestEnemy != null && shortestDistance <= range)
        {
            target = nearestEnemy.transform;
        }
        else
        {
            target = null;
        }
    }

    void Update()
    {
        if (target == null)
            return;

        LockOnTarget();

        if (fireCountdown <= 0f)
        {
            Shoot();
            fireCountdown = 1f / fireRate;
        }

        fireCountdown -= Time.deltaTime;
    }

    void LockOnTarget()
    {
        Vector3 dir = target.position - transform.position;
        Quaternion lookRotation = Quaternion.LookRotation(dir);
        Vector3 rotation = Quaternion.Lerp(partToRotate.rotation, lookRotation, Time.deltaTime * turnSpeed).eulerAngles;
        partToRotate.rotation = Quaternion.Euler(0f, rotation.y, 0f);
    }

    void Shoot()
    {
        if (bombPrefab == null)
        {
            Debug.LogError("Bomb Prefab no está asignado!");
            return;
        }

        GameObject bombGO = Instantiate(bombPrefab, firePoint.position, firePoint.rotation);
        BombProjectile bomb = bombGO.GetComponent<BombProjectile>();

        if (bomb != null)
        {
            bomb.Seek(target, damage, explosionRadius);
        }
    }

    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, range);
    }
}
